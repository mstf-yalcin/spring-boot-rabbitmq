package com.rabbitmq.consumer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
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
    @Value("${rabbitmq.queue}")
    String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    //retry
    @Bean
    public Channel channel(CachingConnectionFactory connectionFactory, RetryTemplate retryTemplate) throws Exception {
        RetryTemplate template = retryTemplate();
        Connection connection = template.execute(
                retryContext -> connectionFactory.createConnection());
        Channel channel = connection.createChannel(false);
        return channel;
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


    //for channel-rabbitTemplate
    //    @Bean
//    public CachingConnectionFactory connectionFactory() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setUri("amqp://user:password@localhost:5672");
//        return connectionFactory;
//    }


    //only channel
    //    @Bean
//    public ConnectionFactory connectionFactory() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
//        var connectionFactory = new ConnectionFactory();
//        connectionFactory.setUri("amqp://user:password@localhost:5672");
//        return connectionFactory;
//    }
//
//    @Bean
//    public Channel channel(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
//        return connectionFactory.newConnection().createChannel();
//    }


}
