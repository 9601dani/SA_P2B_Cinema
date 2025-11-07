package com.codenbugs.cinema.ticketsale.infrastructure.outputadapters.event;

import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.event.TicketEventAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketEventAdapterTest {
    private AmqpTemplate amqpTemplate;
    private Environment env;
    private TicketEventAdapter adapter;

    @BeforeEach
    void setUp() {
        amqpTemplate = mock(AmqpTemplate.class);
        env = mock(Environment.class);

        when(env.getProperty("rabbitmq.exchange", "ticket.exchange")).thenReturn("ticket.exchange");
        when(env.getProperty("rabbitmq.routing.ticket_created", "ticket.created")).thenReturn("ticket.created");

        adapter = new TicketEventAdapter(amqpTemplate, env);
        adapter.init();
    }

    @Test
    void shouldSendPayTicketEvent() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        TicketSaleDomainEntity entity = new TicketSaleDomainEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(50.0),
                BigDecimal.ZERO,
                StateTicket.PENDING_PAYMENT,
                walletId,
                null
        );

        // Act
        adapter.payTicketEvent(entity, ticketId);

        // Assert
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(amqpTemplate, times(1)).convertAndSend(eq("ticket.exchange"), eq("ticket.created"), captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertEquals(walletId, payload.get("walletId"));
        assertEquals(entity.getPriceTotal(), payload.get("amount"));
        assertEquals("debit_ticket", payload.get("typeTransaction"));
        assertEquals(ticketId, payload.get("ticketId"));
    }

    @Test
    void shouldThrowNotificationSendExceptionWhenAmqpFails() {
        // Arrange
        UUID walletId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        TicketSaleDomainEntity entity = new TicketSaleDomainEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(50.0),
                BigDecimal.ZERO,
                StateTicket.PENDING_PAYMENT,
                walletId,
                null
        );

        doThrow(new RuntimeException("AMQP failed")).when(amqpTemplate).convertAndSend(anyString(), anyString(), anyMap());

        // Act & Assert
        NotificationSendException exception = assertThrows(NotificationSendException.class, () -> {
            adapter.payTicketEvent(entity, ticketId);
        });

        assertTrue(exception.getMessage().contains("Error al realizar el pago"));
    }
}
