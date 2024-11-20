package in.rabbitmq.stockService.subscriber;

import in.rabbitmq.stockService.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @RabbitListener(queues = {"${spring.rabbitmq.queue.name}"})
    public void consume(OrderEvent orderEvent) {
        LOGGER.info("Consume order event: {}", orderEvent);
    }
}
