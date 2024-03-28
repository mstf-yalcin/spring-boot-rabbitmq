package com.rabbitmq.consumer.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;


@Service
public class ManualConsumer {

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.queue}")
    String queueName;

    @Value("${rabbitmq.routingKey}")
    String routingKey;

    private final Channel channel;

    private final Logger logger = Logger.getLogger(ManualConsumer.class.getName());

    public ManualConsumer(Channel channel) throws IOException {
        this.channel = channel;
        channel.basicQos(0, 4, true);
    }

    @PostConstruct
    public void startConsuming() throws IOException {

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            Wait();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            System.out.println("Received message: " + message);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });

        System.out.println("waiting...");


    }

    private void Wait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //------------------22222222-------------------------------------
//        @Value("${rabbitmq.queue}")
//         String queueName;
//
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer(CachingConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(messageListener());
//
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        return container;
//    }
//    @Bean
//    public ChannelAwareMessageListener messageListener() {
//        return new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.out.println("Received message: " + msg);
//
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            }
//        };
//    }


}
