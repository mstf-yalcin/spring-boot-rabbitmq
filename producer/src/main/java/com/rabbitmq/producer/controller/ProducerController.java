package com.rabbitmq.producer.controller;


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

    @PostMapping("/{count}")
    public ResponseEntity<String> Publish(@PathVariable int count, @RequestBody Message message) {

        for (int i = 0; i < count; i++)
            producer.Publish(message);

        return ResponseEntity.ok("Success..");
    }

}
