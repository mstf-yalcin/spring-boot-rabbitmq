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

    @Value("${rabbitmq.exchange}")
    String exchange;

    @Value("${rabbitmq.queue}")
    String queueName;

    @Value("${rabbitmq.routingKey}")
    String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
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
        RetryTemplate template = retryTemplate();
        Connection connection = template.execute(
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
//    public TopicExchange topicExchange()
//    {
//        return new TopicExchange(exchange,true,false);
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
