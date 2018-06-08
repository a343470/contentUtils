package com.example.rabbit;

import com.example.rabbit.resource.ResourceService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	ResourceService resourceService;
	@Test
	public void contextLoads() throws  Exception{
//		resourceService.sendByType(0,3252400,"cataLogUtilsinfo","/resource");
//		java.io.File file = new java.io.File("sendFile");
//		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		Map<String, Integer> sendMap = getSendMap();
		for (String key : sendMap.keySet()) {
//			resourceService.sendByType(0,sendMap.get(key),key,"/resource","cms");
			resourceService.sendByType(0,sendMap.get(key),key,"/resource","songLibrary");
			while (isSend()){
				continue;
			}
		}


//		resourceService.sendByType(0,222259,"mv","stable");
//		rabbitTemplate.convertAndSend("detopic","ee","eee");
//		MessagePostProcessor messagePostProcessor =new MessagePostProcessor() {
//			@Override
//			public Message postProcessMessage(Message message) throws AmqpException {
//				MessageProperties messageProperties = message.getMessageProperties();
//				messageProperties.setHeader("routingKey","m.tp.ml-data-changed.dalbum");
//				return message;
//			}
//		};
//		rabbitTemplateWq.convertAndSend("amq.topic","m.tp.ml-data-changed.dalbum", (Object) "cp个",messagePostProcessor);
	}

	private Map<String,Integer> getSendMap(){
		Map<String,Integer> map = new LinkedHashMap<>();
//		map.put("song",2833);
//		map.put("singer",73);
//		map.put("mv",222258);
//		map.put("mvmaterial",6);
//		map.put("wireless-product",7216197);

		map.put("crbt-product",100);

		return map;
	}






	public void main(String[] args) {
		String re = "\\[1-9\\]";
		String jj = "12312321312";
		System.out.println(jj.replace(re,"1"));
	}


	public boolean isSend(){
		int syncCount = resourceService.getRabbitTemplate().execute(new ChannelCallback<AMQP.Queue.DeclareOk>() {
			public AMQP.Queue.DeclareOk doInRabbit(Channel channel) throws Exception {
				return channel.queueDeclarePassive("q.resource.share.aync-resource");
			}
		}).getMessageCount();

		int convertCount = resourceService.getRabbitTemplate().execute(new ChannelCallback<AMQP.Queue.DeclareOk>() {
			public AMQP.Queue.DeclareOk doInRabbit(Channel channel) throws Exception {
				return channel.queueDeclarePassive("q.resource.share.cms-ml-data-convert");
			}
		}).getMessageCount();


		if(syncCount<3000 && convertCount<3000){
			return false;
		}
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}
}
