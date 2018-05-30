package com.example.rabbit.resource;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huyang
 * @Version: 1.0
 * @Date: 16:46 2018/5/10
 */
@RestController
public class ResourceService {
    @Autowired
    RabbitTemplate                             rabbitTemplate;
    @Autowired
    ResourceRepository                       resourceDORepository;
    private static ThreadLocal<String>         pool = new ThreadLocal<>();
    private static Map<String, RabbitTemplate> map  = new HashMap<String, RabbitTemplate>();

    public RabbitTemplate getRabbitTemplate() {
        RabbitTemplate rabbitTemplate1 = map.get(pool.get());
        if (rabbitTemplate1 != null) {
            return rabbitTemplate1;
        }
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("10.25.245.121:8088");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setVirtualHost("" + pool.get());
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        map.put(pool.get(), rabbitTemplate);
        return rabbitTemplate;
    }

    @RequestMapping("/resource/sendByType")
    public String sendByType(int start, int count, String type, String host) {
        pool.set(host);
        int countInteger = 1000;
        int i = (count - start - 1) / countInteger + 1;
        for (int j = 0; j < i; j++) {

           int start1 = start + countInteger * j;
            int count1 = 0;
            if (i - 1 == j) {
                 count1 = count - (start + countInteger * j);
            } else {
                 count1 =countInteger;
            }
            List<Resource> list = resourceDORepository.findAllByType(type,start1,count1);
            int sendNum = 10;
            int c = (list.size() - 1) / sendNum + 1;


            for (int k = 0; k < 100; k++) {
                matching(type, k+"");
            }
            for (int d = 0; d < c; d++) {
                List<Resource> list1;
                if (d == c - 1) {
                    list1 = list.subList(d * sendNum, list.size());
                } else {
                    list1 = list.subList(d * sendNum, (d + 1) * sendNum);
                }
                String sub = "";
                for (Resource resourceDataDo : list1) {
                    sub += resourceDataDo.getId() + "_";
                }
                String substring = sub.substring(0, sub.length() - 1);
                matching(type, substring);
            }
        }
        return "{\"result\":\"老铁没毛病\"}";
    }

    public static void main(String[] args) throws InterruptedException {

        String aaa = "{\"name\":\"1dsadasda   \\\"dsadsa\\\"     \",\"age\":\"11\"}";
        System.out.println(JSONObject.toJSONString(JSONObject.parseObject(aaa,User.class)).replace("\\\"","\""));



      /*  try {
            String s = "1[1{\"name\":\"1\",\"age\":\"11\"},{\"name\":\"1\",\"age\":\"11\"}]";
            List<String> users = JSONObject.parseArray(s, String.class);
            users.stream().forEach(System.out::println);
        }catch (Exception e){
            e.getClass();
            StringBuffer buf = new StringBuffer();
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                buf.append(stackTraceElement.toString()+"          ");
            }
            System.out.println(buf);

            Thread.sleep(10000);
            e.printStackTrace();
        }
*/
    }
    @Data
    public static class User{
        String name;
        String age;
    }

    private String matching(String type, String id) {
        switch (type) {
            case "common-material":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_COMMON_MATERIAL, id);
                break;
            case "tag-template":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_TAG_TEMPLATE, id);
                break;
            case "vrbt-deploy-impproductnotify":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_VRBT_DEPLOY_IMPPRODUCTNOTIFY, id);
                break;
            case "crbt-product":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_CRBT_PRODUCT, id);
                break;
            case "wireless-product":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_WIRELESS_PRODUCT, id);
                break;
            case "cataLogUtilsinfo":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_CCATALOG_INFO, id);
                break;
            case "mv":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_MV, id);
                break;
            case "dalbum":
                send(TopicRabbitConfig.M_TP_SONGLIBRARY_DATA_CHANGED_RESOURCE_DALBUM, id);
                break;
            case "song":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_SONG, id);
                break;
            case "singer":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_SINGER, id);
                break;
            case "album":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_ALBUMS, id);
                break;
            case "picture":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PICTURES, id);
                break;
            case "txt":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TXTS, id);
                break;
            case "link":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_LINKS, id);
                break;
            case "template":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TEMPLATES, id);
                break;
            case "status":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_STATUSS, id);
                break;
            case "channel":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_CHANNELS, id);
                break;
            case "tag":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TAGS, id);
                break;
            case "platform":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PLATFORMS, id);
                break;
            case "tonebox":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TONEBOXS, id);
                break;
            case "dalbum1":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_DALBUMS, id);
                break;
            case "classify":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_CLASSIFYS, id);
                break;
            case "manufacturer":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_MANUFACTURERS, id);
                break;
            case "skin":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_SKINS, id);
                break;
            case "terminal":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TERMINALS, id);
                break;
            case "findterminalprop":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_FINDTERMINALPROPS, id);
                break;
            case "terminaldic":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TERMINALDICS, id);
                break;
            case "findterminalpropValue":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_FINDTERMINALPROPVALUES, id);
                break;
            case "application":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_APPLICATIONS, id);
                break;
            case "apprecommend":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_APPRECOMMENDS, id);
                break;
            case "marketing":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_MARKETINGS, id);
                break;
            case "push":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PUSHS, id);
                break;
            case "startpic":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_STARTPICS, id);
                break;
            case "subscribe":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_SUBSCRIBES, id);
                break;
            case "terminalchannel":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TERMINALCHANNELS, id);
                break;
            case "city":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_CITYS, id);
                break;
            case "province":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PROVINCES, id);
                break;
            case "indiemusiciannote":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_INDIEMUSICIANNOTES, id);
                break;
            case "MiguProduce":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_MIGUPRODUCES, id);
                break;
            case "Concert":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_CONCERTS, id);
                break;
            case "ticketing":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_TICKETINGS, id);
            case "playlist":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PLAYLISTS, id);
                break;
            case "diyring":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_DIYRINGS, id);
                break;
            case "advert":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_ADVERTS, id);
                break;
            case "mvmaterial":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_MVMATERIALS, id);
                break;
            case "prop":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_PROPS, id);
                break;
            case "videoclip":
                send(TopicRabbitConfig.M_TP_CMS_DATA_CHANGED_RESOURCE_VIDEOCLIPS, id);
                break;

        }
        return null;
    }

    private void send(String routingKey, String id) {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setHeader("routingKey", routingKey);
                return message;
            }
        };
        this.getRabbitTemplate().convertAndSend(TopicRabbitConfig.AMQ_TOPIC, routingKey,
            String.valueOf(id), messagePostProcessor);
    }


}
