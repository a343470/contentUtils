package com.example.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huyang
 * @Version: 1.0
 * @Date: 18:57 2018/4/26
 */
public class DelayConfig {
    @Bean
    Exchange exchange(){
       return        new ExchangeBuilder("delay_exchange","x-delayed-message").withArgument("x-delayed-message","topic").build();
    }


    @Bean
    public org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory() {
        CachingConnectionFactory connection = createConnection();
        connection.setVirtualHost("/");
        return connection;
    }

    private CachingConnectionFactory createConnection(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("118.24.119.201:5672");
        connectionFactory.setUsername("cp");
        connectionFactory.setPassword("cp");
        connectionFactory.setPublisherConfirms(true);
        return connectionFactory;
    }
    @Autowired
    Exchange exchange;
    @Autowired
    RabbitTemplate rabbitTemplate;
    // 队列名称
    private final static String EXCHANGE_NAME="delay_exchange";
    private final static String ROUTING_KEY="key_delay";

    @SuppressWarnings("deprecation")
    public static void main(String[] argv) throws Exception {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("118.24.119.201");
        factory.setUsername("cp");
        factory.setPassword("cp");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        // 声明x-delayed-type类型的exchange
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        channel.exchangeDeclare(EXCHANGE_NAME, "x-delayed-message", true,
                false, args);


        Map<String, Object> headers = new HashMap<String, Object>();
        //设置在2016/11/04,16:45:12向消费端推送本条消息
        Date now = new Date();
        Date timeToPublish = new Date("2016/11/04,16:45:12");

        String readyToPushContent = "publish at " + sf.format(now)
                + " \t deliver at " + sf.format(timeToPublish);

        headers.put("x-delay", timeToPublish.getTime() - now.getTime());

        AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder()
                .headers(headers);
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, props.build(),
                readyToPushContent.getBytes());

        // 关闭频道和连接
        channel.close();
        connection.close();
    }
}
