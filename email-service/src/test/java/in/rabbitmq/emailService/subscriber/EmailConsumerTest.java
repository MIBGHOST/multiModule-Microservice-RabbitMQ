package in.rabbitmq.emailService.subscriber;

import in.rabbitmq.emailService.dto.Order;
import in.rabbitmq.emailService.dto.OrderEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class EmailConsumerTest {
//    @Mock
//    private Logger logger;
//
//    @InjectMocks
//    private EmailConsumer emailConsumer;
//
//    @Test
//    void consume() {
//        openMocks(this);
//
//        OrderEvent orderEvent = new OrderEvent();
//        orderEvent.setMessage("consuming message from RabbitMq EmailQueue");
//        orderEvent.setStatus("Pending");
//        orderEvent.setOrder(new Order("123","basket",1,1000));
//
//        emailConsumer.consume(orderEvent);
//
//
//        assertThat(orderEvent.getMessage()).isEqualTo("consuming message from RabbitMq EmailQueue");
//        assertThat(orderEvent.getStatus()).isEqualTo("Pending");
//        assertThat(orderEvent.getOrder().getOrderId()).isEqualTo("123");
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private EmailConsumer emailConsumer;
    @Container
    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.3-management");

    @DynamicPropertySource
    static void rabbitProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }
    @BeforeAll
    static void startContainer() {
        rabbitMQContainer.start();
    }

    @AfterAll
    static void stopContainer() {
        rabbitMQContainer.stop();
    }
    @BeforeEach
    void setUp() {
        OrderEvent event = new OrderEvent("Success","Order confirmed", new Order("123","basket", 1,1000));
        rabbitTemplate.convertAndSend("order-exchange","email-routing-key", event);
    }

    @Test
    void consume() {
        rabbitTemplate.receiveAndConvert("email-queue");

        Order order = new Order("123","basket", 1,1000);
        OrderEvent event = new OrderEvent("Success","Order confirmed", order);
        emailConsumer.consume(event);
        assertEquals(order, event.getOrder());
        assertEquals("message sent", event.getMessage());
        assertEquals("Pending", event.getStatus());

    }
}