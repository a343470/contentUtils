package com.example.rabbit;

import com.example.rabbit.resource.ResourceService;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	ResourceService resourceService;
	@Test
	public void contextLoads() {
		resourceService.sendByType(0,1,"picture","/hy");
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


	public static void main(String[] args) {
		User user = new User();
		Integer i = null;
		user.setI(i);
		System.out.println(i);
	}

	@Data
	public static class User{
		int i =-1 ;
		int  j = -1;
	}

}
