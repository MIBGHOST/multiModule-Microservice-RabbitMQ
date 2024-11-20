package in.rabbitmq.emailService.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

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
