package in.rabbitmq.orderService.controller;

import in.rabbitmq.orderService.dto.Order;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    private static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:4.0.3-management");
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    private int port;


    @BeforeAll
    static void setUp() {
        rabbitMQContainer.start();
    }

    @AfterAll
    static void tearDown() {
        rabbitMQContainer.stop();
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void testShouldPlaceOrderSuccessfully() {
        Order order = new Order(null, "basketball", 1, 2000);
        String url = "http://localhost:" + port + "/api/v1/orders";

        //send the request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, order, String.class);

        //assert the http response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
    }

    @Test
    void testShouldDoHealthCheck() {
        String url = "http://localhost:" + port + "/api/v1/health";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).contains("health check ok");
    }
}