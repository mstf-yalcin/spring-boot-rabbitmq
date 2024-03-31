//package com.rabbitmq.consumer.consumer;
//
//import com.rabbitmq.consumer.model.Message;
//import org.springframework.amqp.core.ExchangeTypes;
//import org.springframework.amqp.rabbit.annotation.*;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RabbitListenerConsumer {
//
//    @RabbitListener(
//            queues = "${rabbitmq.direct.queue}",
//            containerFactory = "directRabbitListenerContainerFactory"
//    )
//    public void receiveMessageFromDirectExchange(Message message) {
//        System.out.println("Received message from 'Direct' exchange: " + message);
//    }
//
//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange(value = "${rabbitmq.fanout.exchange}", type = ExchangeTypes.FANOUT)
//            )
//
//    )
//    public void receiveMessageFromFanOutExchange(Message message) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println("Received message from 'FanOut' exchange: " + message);
//    }
//
//    @RabbitListener(
//            bindings = @QueueBinding(
//                    value = @Queue(value = "${rabbitmq.topic.queue}", durable = "true", exclusive = "false", autoDelete = "false"),
//                    exchange = @Exchange(value = "${rabbitmq.topic.exchange}", type = ExchangeTypes.TOPIC),
//                    key = "${rabbitmq.topic.routingKey}"
//            )
//    )
//    public void receiveMessageTopicExchange(Message message) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println("Received message from 'Topic' exchange: " + message);
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "${rabbitmq.header.queue}", durable = "true", exclusive = "false", autoDelete = "false"),
//            exchange = @Exchange(value = "${rabbitmq.header.exchange}", type = ExchangeTypes.HEADERS, durable = "true", autoDelete = "false"),
//            arguments = {
//                    @Argument(name = "type", value = "a"),
//                    @Argument(name = "data", value = "test"),
//                    @Argument(name = "x-match", value = "any")
//            }
//    ))
//    public void receiverMessageHeaderExchange(Message message) throws InterruptedException {
//        Thread.sleep(1000);
//        System.out.println("Received message from 'Header' exchange: " + message);
//    }
//
//}