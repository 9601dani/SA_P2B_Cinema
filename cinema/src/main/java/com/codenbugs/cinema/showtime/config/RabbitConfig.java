package com.codenbugs.cinema.showtime.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // === SHOWTIME ===
    @Value("${rabbitmq.exchange.showTime:showTime.exchange}")
    private String showTimeExchange;

    @Value("${rabbitmq.routing.showTime_created:showTime.created}")
    private String showTimeCreatedRoutingKey;

    @Value("${rabbitmq.queues.showTime_created_notify:showTime.created.queue}")
    private String showTimeCreatedQueue;

    @Bean
    public TopicExchange showTimeExchange() {
        return new TopicExchange(showTimeExchange, true, false);
    }

    @Bean
    public Queue showTimeCreatedQueue() {
        return new Queue(showTimeCreatedQueue, true);
    }

    @Bean
    public Binding showTimeBinding(Queue showTimeCreatedQueue, TopicExchange showTimeExchange) {
        return BindingBuilder.bind(showTimeCreatedQueue)
                .to(showTimeExchange)
                .with(showTimeCreatedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }
}
