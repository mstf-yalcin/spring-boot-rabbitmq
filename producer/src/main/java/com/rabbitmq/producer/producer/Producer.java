package com.rabbitmq.producer.producer;

import com.rabbitmq.producer.model.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    @Value("${rabbitmq.exchange}")
    String exchange;
    @Value("${rabbitmq.routingKey}")
    String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void Publish(Message message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
