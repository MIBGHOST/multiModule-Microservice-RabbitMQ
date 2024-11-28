package in.rabbitmq.emailService.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RabbitMqConfigTest {
    @Autowired
    private ApplicationContext applicationContext;


    @Test
    void testEmailQueueBean() {
        Queue queue = applicationContext.getBean("emailQueue", Queue.class);
        assertThat(queue.getName()).isEqualTo("email-queue");
    }

    @Test
    void testOrderExchangeBean() {
        TopicExchange exchange = applicationContext.getBean("orderExchange", TopicExchange.class);
        assertThat(exchange.getName()).isEqualTo("order-exchange");

    }

    @Test
    void testEmailBindingBean() {
        Binding binding = applicationContext.getBean("emailBinding", Binding.class);
        assertThat(binding.getExchange()).isEqualTo("order-exchange");
    }
}