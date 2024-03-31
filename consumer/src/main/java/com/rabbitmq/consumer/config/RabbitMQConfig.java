package com.rabbitmq.consumer.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;


@Configuration
public class RabbitMQConfig {
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> directRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(2);
        factory.setPrefetchCount(5);
        factory.setGlobalQos(true);
        return factory;
    }

    //retry
    @Bean
    public Channel channel(CachingConnectionFactory connectionFactory, RetryTemplate retryTemplate) throws Exception {
        Connection connection = retryTemplate.execute(
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





//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, RetryTemplate retryTemplate) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setRetryTemplate(retryTemplate);
//        return rabbitTemplate;
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
