package in.rabbitmq.orderService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

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

    /*
    * bean for OrderQueue
    */
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE);
    }

    /*
     * bean for emailQueue
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE);
    }

    /*
     * bean for OrderExchange
     */
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    /*
     * bean for OrderBinding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder
                .bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    /*
    * bean for emailBinding
    */
    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(orderExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    /*
     * bean for MessageConverter to json
     */
    @Bean
    public MessageConverter messageConverter() {
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
