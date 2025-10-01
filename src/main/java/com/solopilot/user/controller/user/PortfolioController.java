package com.solopilot.user.controller.user;

import com.autopilot.enums.EventType;
import com.autopilot.models.payload.QueuePayload;
import com.solopilot.user.config.RabbitMQConfig;
import com.solopilot.user.messaging.publisher.UserMessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final RabbitMQConfig rabbitMQConfig;
    private final UserMessagePublisher userMessagePublisher;

    @GetMapping("/hero-section")
    public void heroSection() {
        QueuePayload<String> payload = QueuePayload.<String>builder()
            .eventType(EventType.USER_CREATED)
            .payload("Hero section message")
            .build();
        userMessagePublisher.publishWithRoutingKey(
            rabbitMQConfig.getNotificationExchangeName(),
            rabbitMQConfig.getEmailRoutingKey(),
            payload,
            null
        );
    }
}
