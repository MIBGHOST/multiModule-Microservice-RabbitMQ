package in.rabbitmq.emailService.subscriber;

import in.rabbitmq.emailService.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class emailConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(emailConsumer.class);

    @RabbitListener(queues = {"${spring.rabbitmq.email.name}"})
    public void consume(OrderEvent orderEvent) {
        LOGGER.info("Consume order event: {}", orderEvent);
    }
}
