package com.example.rabbit;



import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: huyang
 * @Version: 1.0
 * @Date: 16:07 2018/4/25
 */
@Component
//@org.springframework.amqp.rabbit.annotation.RabbitListener(bindings = {@QueueBinding(value = @Queue(value = "q.resource.share.aync_resource",
//        arguments = {@Argument(name = "x-dead-letter-exchange", value = "amq.topic"),@Argument(name = "x-dead-letter-routing-key", value = "m.tp.ml-data-convert-changed.3")}), exchange = @Exchange(value = "amq.topic",type = ExchangeTypes.TOPIC, durable = "false")
//)})
@org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "q.resource.share.aync_resource")
public class RabbitListener {
    @RabbitHandler
    public void receiveMessage(String id
           // ,         Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) throws IOException {
//        MessagePostProcessor messagePostProcessor =new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                MessageProperties messageProperties = message.getMessageProperties();
//                messageProperties.setHeader("routingKey",routingKey);
//                return message;
//            }
//        };
       int i = 1/0;
//        channel.basicNack(tag,true,false);
//        channel.basicNack(tag.getEnvelope().getDeliveryTag(), false, true);
  /*      rabbitTemplateCp.convertAndSend("amq.topic",routingKey,id,messagePostProcessor);
        rabbitTemplateHy.convertAndSend("amq.topic",routingKey,id,messagePostProcessor);
        rabbitTemplateWq.convertAndSend("amq.topic",routingKey,id,messagePostProcessor);
        rabbitTemplateDsy.convertAndSend("amq.topic",routingKey,id,messagePostProcessor);*/
    }
}
