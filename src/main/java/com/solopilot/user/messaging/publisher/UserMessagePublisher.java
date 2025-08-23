package com.solopilot.user.messaging.publisher;

import com.autopilot.config.exception.ApplicationException;
import com.autopilot.config.exception.ApplicationExceptionTypes;
import com.autopilot.config.logging.AppLogger;
import com.autopilot.messaging.IBaseQueuePublisher;
import com.autopilot.models.payload.QueuePayload;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class UserMessagePublisher implements IBaseQueuePublisher {

    private final AppLogger log = new AppLogger(LoggerFactory.getLogger(UserMessagePublisher.class));

    private final RabbitTemplate rabbitTemplate;

    public UserMessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String queueName, QueuePayload<?> payload, Map<String, Object> headers) {
        log.warn("publish(queueName={}, payload={}) called but not supported", queueName, payload);
        throw new ApplicationException(ApplicationExceptionTypes.METHOD_NOT_SUPPORTED.code(),
                ApplicationExceptionTypes.METHOD_NOT_SUPPORTED.status(),
                ApplicationExceptionTypes.METHOD_NOT_SUPPORTED.message().formatted("publishWithRoutingKey"));
    }

    @Override
    public CompletableFuture<Void> publishAsync(String queueName, QueuePayload<?> payload, Map<String, Object> headers) {
        log.info("Publishing async to queue={} with payload={}", queueName, payload);
        return CompletableFuture.runAsync(() -> {
            publish(queueName, payload, headers);
        });
    }

    @Override
    public void publishWithRoutingKey(String exchangeName, String routingKey, QueuePayload<?> payload, Map<String, Object> headers) {
        log.info("Publishing message -> exchange={}, routingKey={}, payload={}", exchangeName, routingKey, payload);

        rabbitTemplate.convertAndSend(exchangeName, routingKey, payload, metadata -> {
            if (headers != null) {
                headers.forEach((key, value) -> {
                    log.debug("Adding header: {}={}", key, value);
                    metadata.getMessageProperties().setHeader(key, value);
                });
            }
            return metadata;
        });

        log.info("Message successfully published to exchange={} with routingKey={}", exchangeName, routingKey);
    }

    @Override
    public CompletableFuture<Void> publishAsyncWithRoutingKey(String exchangeName, String routingKey, QueuePayload<?> payload, Map<String, Object> headers) {
        log.info("Publishing async -> exchange={}, routingKey={}, payload={}", exchangeName, routingKey, payload);
        return CompletableFuture.runAsync(() -> {
            publishWithRoutingKey(exchangeName, routingKey, payload, headers);
        });
    }
}
