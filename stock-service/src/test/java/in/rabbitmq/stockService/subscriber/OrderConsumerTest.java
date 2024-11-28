package in.rabbitmq.stockService.subscriber;

import in.rabbitmq.stockService.dto.Order;
import in.rabbitmq.stockService.dto.OrderEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class OrderConsumerTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderConsumer orderConsumer;

    @Container
    static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.3-management");

    @BeforeAll
    static void setUp() {
        rabbitMQContainer.start();
    }
    @AfterAll
    static void tearDown() {
        rabbitMQContainer.stop();
    }
    @BeforeEach
    void setUpBeforeEach() {
        OrderEvent event = new OrderEvent("Success","update the stock", new Order("123","basket", 1,1000));
        rabbitTemplate.convertAndSend("order-exchange","stock-routing-key",event);
    }
    @Test
    void consume() {
        rabbitTemplate.receiveAndConvert("stock-queue");

        Order order = new Order("123","basket", 1,1000);
        OrderEvent event = new OrderEvent("Success","update the stock", order);

        orderConsumer.consume(event);

        assertEquals(order, event.getOrder());
        System.out.println(event.getOrder());
        assertEquals("update the stock", event.getMessage());
        System.out.println(event.getMessage());
        assertEquals("Success", event.getStatus());
        System.out.println(event.getStatus());

    }
}