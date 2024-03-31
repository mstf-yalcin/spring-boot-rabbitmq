package com.rabbitmq.producer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.producer.model.Message;
import com.rabbitmq.producer.producer.Producer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/producer")
public class ProducerController {
    private final Producer producer;

    public ProducerController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("DirectExchange/{count}")
    public ResponseEntity<String> PublishDirect(@PathVariable int count, @RequestBody Message message) {

        for (int i = 0; i < count; i++) {
            message.setUserId(message.getUserId() + i);
            producer.PublishDirectExchange(message);
        }
        return ResponseEntity.ok("Direct Success..");
    }

    @PostMapping("TopicExchange/{count}")
    public ResponseEntity<String> PublishTopic(@PathVariable int count, @RequestBody Message message) {

        for (int i = 0; i < count; i++)
            producer.PublishTopicExchange(message);

        return ResponseEntity.ok("Topic Success..");
    }

    @PostMapping("FanoutExchange/{count}")
    public ResponseEntity<String> PublishFanOut(@PathVariable int count, @RequestBody Message message) {

        for (int i = 0; i < count; i++)
            producer.PublishFanOutExchange(message);

        return ResponseEntity.ok("Fanout Success..");
    }

    @PostMapping("HeaderExchange/{count}")
    public ResponseEntity<String> PublishHeader(@PathVariable int count, @RequestBody Message message) throws JsonProcessingException {

        for (int i = 0; i < count; i++)
            producer.PublishHeaderExchange(message);

        return ResponseEntity.ok("Header Success..");
    }

}
