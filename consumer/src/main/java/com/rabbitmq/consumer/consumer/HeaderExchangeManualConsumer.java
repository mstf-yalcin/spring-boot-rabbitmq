package com.rabbitmq.consumer.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class HeaderExchangeManualConsumer {

    @Value("${rabbitmq.header.queue}")
    String queue;
    @Value("${rabbitmq.header.exchange}")
    String headerExchange;

    private final Channel channel;

    private final Logger logger = Logger.getLogger(HeaderExchangeManualConsumer.class.getName());

    public HeaderExchangeManualConsumer(Channel channel) throws IOException {
        this.channel = channel;
    }

    @PostConstruct
    public void startConsuming() throws IOException {

        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "a");
        headers.put("data", "test");
        headers.put("x-match", "all");

        channel.queueDeclare(queue, true, false, false, headers);
        channel.queueBind(queue, headerExchange, "");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            Wait();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message from 'Header' exchange: " + message);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queue, false, deliverCallback, consumerTag -> {
        });

        System.out.println("waiting header exchange...");

    }

    private void Wait() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
