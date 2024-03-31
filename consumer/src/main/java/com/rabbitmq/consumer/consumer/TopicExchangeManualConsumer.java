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
public class TopicExchangeManualConsumer {
    @Value("${rabbitmq.topic.queue}")
    String queueName;

    @Value("${rabbitmq.topic.exchange}")
    String topicExchange;

    String topicRoutingKey;

    String topicRoutingKey2;

    private final Channel channel;

    private final Logger logger = Logger.getLogger(DirectExchangeManualConsumer.class.getName());

    public TopicExchangeManualConsumer(Channel channel) throws IOException {
        this.channel = channel;


    }

    @PostConstruct
    public void startConsuming() throws IOException {

        topicRoutingKey = "#.topic";
        topicRoutingKey2 = "*.routingKey.*";

        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, topicExchange, topicRoutingKey);
        channel.basicQos(0, 5, true);


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            Wait();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from 'Topic' exchange: " + message);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, false, deliverCallback, consumerTag -> {
        });

        System.out.println("waiting topic exchange...");

    }

    private void Wait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
