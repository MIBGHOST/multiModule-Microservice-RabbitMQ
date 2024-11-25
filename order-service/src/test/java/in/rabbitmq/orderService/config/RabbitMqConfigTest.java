package in.rabbitmq.orderService.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class RabbitMqConfigTest {

    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.3-management");

    @Value("${spring.rabbitmq.queue.name}")
    private String ORDER_QUEUE ;
    @Value("${spring.rabbitmq.topicExchange.name}")
    private String ORDER_EXCHANGE;
    @Value("${spring.rabbitmq.routing.key}")
    private String ORDER_ROUTING_KEY;
    @Value("${spring.rabbitmq.email.name}")
    private String EMAIL_QUEUE ;
    @Value("${spring.rabbitmq.email-routing.key}")
    private String EMAIL_ROUTING_KEY;

    @BeforeAll
    static void beforeAll() {
        rabbitMQContainer.start();
    }

    @AfterAll
    static void afterAll() {
        System.out.println(rabbitMQContainer.getHost());
        System.out.println(rabbitMQContainer.getAmqpPort());
        rabbitMQContainer.stop();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void orderQueueBeanShouldBeCreated() {
        assertNotNull(ORDER_QUEUE);
        assertEquals("order-queue", ORDER_QUEUE);
    }

    @Test
    void emailQueueBeanShouldBeCreated() {
        assertNotNull(EMAIL_QUEUE);
        assertEquals("email-queue", EMAIL_QUEUE);
    }

    @Test
    void orderExchangeBeanShouldBeCreated() {
        assertNotNull(ORDER_EXCHANGE);
        assertEquals("order-exchange", ORDER_EXCHANGE);
    }

    @Test
    void orderBindingBeanShouldBeCreated() {
        assertNotNull(ORDER_ROUTING_KEY);
        assertEquals("order-routing-key", ORDER_ROUTING_KEY);
    }

    @Test
    void emailBindingBeanShouldBeCreated() {
        assertNotNull(EMAIL_ROUTING_KEY);
        assertEquals("email-routing-key", EMAIL_ROUTING_KEY);
    }
}