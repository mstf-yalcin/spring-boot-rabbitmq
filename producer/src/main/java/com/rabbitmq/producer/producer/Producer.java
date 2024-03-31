package com.rabbitmq.producer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.producer.model.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class Producer {
    @Value("${rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${rabbitmq.direct.routingKey}")
    String directRoutingKey;

    @Value("${rabbitmq.fanout.exchange}")
    String fanoutExchange;

    @Value("${rabbitmq.topic.exchange}")
    String topicExchange;

    @Value("${rabbitmq.topic.routingKey}")
    String topicRoutingKey;

    @Value("${rabbitmq.header.exchange}")
    String headerExchange;

    private final RabbitTemplate rabbitTemplate;
    public final ObjectMapper mapper;

    public Producer(RabbitTemplate rabbitTemplate, ObjectMapper mapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
    }

    public void PublishDirectExchange(Message message) {
        rabbitTemplate.convertAndSend(directExchange, directRoutingKey, message);
    }

    public void PublishFanOutExchange(Message message) {
        rabbitTemplate.convertAndSend(fanoutExchange, "", message);
    }

    public void PublishTopicExchange(Message message) {
        rabbitTemplate.convertAndSend(topicExchange, topicRoutingKey, message);
    }

    public void PublishHeaderExchange(Message message) throws JsonProcessingException {

        String jsonMessage = mapper.writeValueAsString(message);

        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "a");
        headers.put("data", "test");

        MessageProperties properties = new MessageProperties();
        properties.setHeaders(headers);
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);

        org.springframework.amqp.core.Message msg = new org.springframework.amqp.core.Message(jsonMessage.getBytes(), properties);

        rabbitTemplate.convertAndSend(headerExchange, "", msg);
    }
}
