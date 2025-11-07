package com.codenbugs.cinema.cinema.infrastructure.outputadapters.event;

import com.codenbugs.cinema.cinema.application.ports.output.CinemaCreateWalletEventPort;
import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateWalletEventPublisherTest {
    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private Environment env;

    private CreateWalletEventPublisher publisher;

    @BeforeEach
    void setUp() {
        when(env.getProperty("rabbitmq.exchange.wallet", "wallet.exchange"))
                .thenReturn("wallet.exchange");
        when(env.getProperty("rabbitmq.routing.wallet_created", "wallet.created"))
                .thenReturn("wallet.created");

        publisher = new CreateWalletEventPublisher(amqpTemplate, env);
    }

    @Test
    void shouldPublishWalletEventSuccessfully() {
        UUID cinemaId = UUID.randomUUID();

        publisher.publishCinemaCreateWallet(cinemaId);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(amqpTemplate, times(1))
                .convertAndSend(eq("wallet.exchange"), eq("wallet.created"), captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertEquals(cinemaId, payload.get("ownerId"));
        assertEquals("cinema", payload.get("ownerType"));
    }

    @Test
    void shouldThrowNotificationSendExceptionWhenAmqpFails() {
        UUID cinemaId = UUID.randomUUID();

        doThrow(new RuntimeException("AMQP error"))
                .when(amqpTemplate)
                .convertAndSend(anyString(), anyString(), ArgumentMatchers.<Object>any());

        assertThrows(NotificationSendException.class,
                () -> publisher.publishCinemaCreateWallet(cinemaId));
    }
}
