package com.rabbitmq.consumer.consumer;

import com.rabbitmq.consumer.model.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

//    @RabbitListener(queues = "test-queue")
    public void receiveMessage(Message message) {
        System.out.println("Received message: " + message);
    }
}