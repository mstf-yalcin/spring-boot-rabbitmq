package com.rabbitmq.producer;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.producer.model.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
public class ProducerApplication implements CommandLineRunner {

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.queue}")
    String queueName;

    @Autowired
    private Channel channel;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

        channel.queueDeclare(queueName, true, false, false, null);
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true, false, false, null);
    }


}
