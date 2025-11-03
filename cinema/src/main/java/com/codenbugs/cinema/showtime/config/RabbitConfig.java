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

    // === WALLET ===
    @Value("${rabbitmq.exchange.wallet:wallet.exchange}")
    private String walletExchange;

    @Value("${rabbitmq.routing.wallet_created:wallet.created}")
    private String walletRoutingKey;

    @Value("${rabbitmq.queues.wallet_created_notify:wallet.created.queue}")
    private String walletRegisterQueue;

    // === TICKET ===
    @Value("${rabbitmq.exchange.ticket:ticket.exchange}")
    private String ticketExchange;

    @Value("${rabbitmq.routing.ticket_created:ticket.created}")
    private String ticketCreatedRoutingKey;

    @Value("${rabbitmq.queues.ticket_created_notify:ticket.created.queue}")
    private String ticketCreatedQueue;

    // === STATE TICKET ===
    @Value("${rabbitmq.exchange.stateTicket:stateTicket.exchange}")
    private String stateTicketExchange;

    @Value("${rabbitmq.routing.stateTicket_created:stateTicket.created}")
    private String stateTicketCreatedRoutingKey;

    @Value("${rabbitmq.queues.stateTicket_created_notify:stateTicket.created.queue}")
    private String stateTicketCreatedQueue;

    // === SHOWTIME ===
    @Bean
    public TopicExchange showTimeTopicExchange() {
        return new TopicExchange(showTimeExchange, true, false);
    }

    @Bean
    public Queue showTimeTopicCreatedQueue() {
        return new Queue(showTimeCreatedQueue, true);
    }

    @Bean
    public Binding showTimeTopicBinding(Queue showTimeTopicCreatedQueue, TopicExchange showTimeTopicExchange) {
        return BindingBuilder.bind(showTimeTopicCreatedQueue)
                .to(showTimeTopicExchange)
                .with(showTimeCreatedRoutingKey);
    }

    // === WALLET CONFIGURATION ===
    @Bean
    public TopicExchange walletTopicExchange() {
        return new TopicExchange(walletExchange, true, false);
    }

    @Bean
    public Queue walletTopicQueue() {
        return new Queue(walletRegisterQueue, true);
    }

    @Bean
    public Binding walletTopicBinding(Queue walletTopicQueue, TopicExchange walletTopicExchange) {
        return BindingBuilder.bind(walletTopicQueue)
                .to(walletTopicExchange)
                .with(walletRoutingKey);
    }

    // === TICKET CONFIGURATION ===
    @Bean
    public TopicExchange ticketTopicExchange() {
        return new TopicExchange(ticketExchange, true, false);
    }

    @Bean
    public Queue ticketTopicQueue() {
        return new Queue(ticketCreatedQueue, true);
    }

    @Bean
    public Binding ticketTopicBinding(Queue ticketTopicQueue, TopicExchange ticketTopicExchange) {
        return BindingBuilder.bind(ticketTopicQueue)
                .to(ticketTopicExchange)
                .with(ticketCreatedRoutingKey);
    }

    // === STATE TICKET CONFIGURATION ===
    @Bean
    public TopicExchange stateTicketTopicExchange() {
        return new TopicExchange(stateTicketExchange, true, false);
    }

    @Bean
    public Queue stateTicketTopicQueue() {
        return new Queue(stateTicketCreatedQueue, true);
    }

    @Bean
    public Binding stateTicketTopicBinding(Queue stateTicketTopicQueue, TopicExchange stateTicketTopicExchange) {
        return BindingBuilder.bind(stateTicketTopicQueue)
                .to(stateTicketTopicExchange)
                .with(stateTicketCreatedRoutingKey);
    }


    // === COMMON CONFIGURATION ===

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
