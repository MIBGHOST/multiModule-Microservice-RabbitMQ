package in.rabbitmq.orderService.publisher;

import in.rabbitmq.orderService.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    @Value("${spring.rabbitmq.topicExchange.name}")
    private String ORDER_EXCHANGE;
    @Value("${spring.rabbitmq.routing.key}")
    private String ORDER_ROUTING_KEY;
    @Value("${spring.rabbitmq.email-routing.key}")
    private String EMAIL_ROUTING_KEY;

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(OrderEvent orderEvent) {
        LOGGER.info("Sending order: {}", orderEvent);

        //send an order event to order queue
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, orderEvent);

        //send an order event to email queue
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, EMAIL_ROUTING_KEY, orderEvent);
    }
}
