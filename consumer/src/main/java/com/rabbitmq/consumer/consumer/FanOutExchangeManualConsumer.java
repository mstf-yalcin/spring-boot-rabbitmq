package com.rabbitmq.consumer.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@Service
public class FanOutExchangeManualConsumer {
    String randomQueueName;

    @Value("${rabbitmq.fanout.exchange}")
    String fanoutExchange;

    private final Channel channel;

    private final Logger logger = Logger.getLogger(com.rabbitmq.consumer.consumer.FanOutExchangeManualConsumer.class.getName());

    public FanOutExchangeManualConsumer(Channel channel) throws IOException {
        this.channel = channel;
        randomQueueName = channel.queueDeclare().getQueue();

        channel.basicQos(0, 5, true);
    }

    @PostConstruct
    public void startConsuming() throws IOException {

        channel.queueBind(randomQueueName, fanoutExchange, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            Wait();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from 'FanOut' exchange: " + message);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(randomQueueName, false, deliverCallback, consumerTag -> {
        });

        System.out.println("waiting fanout exchange...");

    }

    private void Wait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
