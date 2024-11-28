package in.rabbitmq.emailService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.email.name}")
    private String EMAIL_QUEUE;
    @Value("${spring.rabbitmq.exchange.name}")
    private String ORDER_EXCHANGE;
    @Value("${spring.rabbitmq.email-routing.key}")
    private String EMAIL_ROUTING_KEY;

    
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE,false);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue())
                .to(orderExchange())
                .with(EMAIL_ROUTING_KEY);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        return rabbitTemplate;
    }

    /*
     * bean for MessageConverter to json
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter();
    }

    //infrastructure beans required in order to work with rabbitMQ
    /*
     * ConnectionFactory
     * RabbitTemplate
     * RabbitAdmin
     */
    ///  but springboot autoconfiguration handles all this by default, and we don't have to manually configure these beans.
}
