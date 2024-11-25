package in.rabbitmq.orderService.publisher;

import in.rabbitmq.orderService.dto.Order;
import in.rabbitmq.orderService.dto.OrderEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@EnableRabbit
class OrderProducerTest {
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.3-management");

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderProducer orderProducer;

    @Value("${spring.rabbitmq.topicExchange.name}")
    private String ORDER_EXCHANGE;
    @Value("${spring.rabbitmq.routing.key}")
    private String ORDER_ROUTING_KEY;
    @Value("${spring.rabbitmq.email-routing.key}")
    private String EMAIL_ROUTING_KEY;

    @BeforeAll
    static void setUp() {
        rabbitMQContainer.start();
    }
    @AfterAll
    static void tearDown() {
        System.out.println(rabbitMQContainer.getAdminUsername() + " " + rabbitMQContainer.getAdminPassword() + " " + rabbitMQContainer.isRunning() + " Listening on " + rabbitMQContainer.getAmqpPort());
        rabbitMQContainer.stop();
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @Test
    void testShouldSendOrderEventToRabbitmq() {
        OrderEvent orderEvent = new OrderEvent("PENDING", "message sending to RabbitMq", new Order("1234","basketball", 5, 10000));

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, orderEvent);

        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, EMAIL_ROUTING_KEY, orderEvent);
        assertDoesNotThrow(()-> orderProducer.send(orderEvent));
    }
}