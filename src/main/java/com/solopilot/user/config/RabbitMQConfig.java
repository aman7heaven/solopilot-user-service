package com.solopilot.user.config;

import com.autopilot.config.logging.AppLogger;
import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.LoggerFactory;

@Configuration
@EnableRabbit
@Data
public class RabbitMQConfig {
    private static final AppLogger log = new AppLogger(LoggerFactory.getLogger(RabbitMQConfig.class));

    @Value("${app.rabbitmq.queues.email}")
    private String emailQueueName;

    @Value("${app.rabbitmq.exchanges.notification}")
    private String notificationExchangeName;

    @Value("${app.rabbitmq.routing-keys.email}")
    private String emailRoutingKey;

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        log.info("Creating RabbitTemplate with custom message converter and provided connection factory: {}", connectionFactory.getClass().getName());
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        log.info("RabbitTemplate initialized successfully.");
        return rabbitTemplate;
    }

    @Bean
    public Queue emailQueue() {
        log.info("creating email queue: '{}'", emailQueueName);
        return new Queue(emailQueueName, true);
    }

    @Bean
    public TopicExchange notificationExchange() {
        log.info("creating notification exchange: '{}'", notificationExchangeName);
        return new TopicExchange(notificationExchangeName, true, false);
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, TopicExchange notificationExchange) {
        log.info("Binding queue '{}' to exchange '{}' with routing key '{}'", emailQueue.getName(), notificationExchange.getName(), emailRoutingKey);
        Binding binding = BindingBuilder.bind(emailQueue).to(notificationExchange).with(emailRoutingKey);
        log.info("Binding created successfully: {}", binding);
        return binding;
    }
}
