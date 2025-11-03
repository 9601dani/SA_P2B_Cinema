package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.event;

import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import com.codenbugs.cinema.ticketsale.application.ports.output.PayTicketEventPort;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketEventAdapter implements PayTicketEventPort {

    private final AmqpTemplate amqpTemplate;
    private final Environment env;

    private String exchange;
    private String routingKey;

    @PostConstruct
    public void init() {
        this.exchange = env.getProperty("rabbitmq.exchange", "ticket.exchange");
        this.routingKey = env.getProperty("rabbitmq.routing.ticket_created", "ticket.created");
    }

    @Override
    public void payTicketEvent(TicketSaleDomainEntity entity, UUID ticketId) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("walletId", entity.getWalletId());
            payload.put("amount", entity.getPriceTotal());
            payload.put("typeTransaction", "debit_ticket");
            payload.put("ticketId", ticketId);
            amqpTemplate.convertAndSend(exchange, routingKey, payload);
        } catch (Exception e) {
            throw  new NotificationSendException("Error al realizar el pago del bloque de publicidad.", e);
        }
    }
}
