package com.rabbitmq.producer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.direct.exchange}")
    String directExchange;

    @Value("${rabbitmq.direct.queue}")
    String directQueueName;

    @Value("${rabbitmq.direct.routingKey}")
    String directRoutingKey;

    @Value("${rabbitmq.fanout.exchange}")
    String fanoutExchange;

    @Value("${rabbitmq.topic.exchange}")
    String topicExchange;

    @Value("${rabbitmq.header.exchange}")
    String headerExchange;


    //Direct Exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchange, true, false);
    }

    @Bean
    public Queue directQueue() {
        return new Queue(directQueueName, true, false, false);
    }

    @Bean
    public Binding directBinding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(directRoutingKey);
    }


    //FanOut Exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange, true, false);
    }


    //Topic Exchange
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange, true, false);
    }

    //Header Exchange
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(headerExchange, true, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory, MessageConverter converter, RetryTemplate retryTemplate) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        return rabbitTemplate;
    }

    @Bean
    public Channel channel(CachingConnectionFactory connectionFactory, RetryTemplate retryTemplate) throws Exception {
        Connection connection = retryTemplate.execute(
                retryContext -> connectionFactory.createConnection());
        Channel channel = connection.createChannel(false);
        return channel;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(5000);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }


    //    @Bean
//    public Channel channel(CachingConnectionFactory connectionFactory) throws Exception {
//        Channel channel = connectionFactory.createConnection().createChannel(false);
//        return channel;
//    }
//    @Bean
//    public ConnectionFactory factory() throws URISyntaxException {
//        var uri=new URI(rabbitUri);
//        return new com.rabbitmq.client.ConnectionFactory().setUri(uri);
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        return new RabbitTemplate(connectionFactory);
//    }
}
