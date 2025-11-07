package com.codenbugs.cinema.showtime.infrastructure.outputadapters.event;

import com.codenbugs.cinema.showtime.application.ports.output.NotificationCreatedShowTimeEventPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationNewShowTimeEventPublisherTest {
    private AmqpTemplate amqpTemplate;
    private Environment env;
    private NotificationCreatedShowTimeEventPort publisher;

    @BeforeEach
    void setUp() {
        amqpTemplate = mock(AmqpTemplate.class);
        env = mock(Environment.class);

        when(env.getProperty("rabbitmq.exchange", "showTime.exchange")).thenReturn("test.exchange");
        when(env.getProperty("rabbitmq.routing.showTime_created", "showTime.created")).thenReturn("test.routingKey");

        publisher = new NotificationNewShowTimeEventPublisher(amqpTemplate, env);
    }

    @Test
    void shouldPublishMessageSuccessfully() {
        ShowTimeDomainEntity showTime = new ShowTimeDomainEntity(
                UUID.randomUUID(),
                new BigDecimal("50.00"),
                UUID.randomUUID(),
                LocalDateTime.now().plusHours(1)
        );

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        publisher.publisherNewShowtimeCustomers(showTime);

        verify(amqpTemplate, times(1))
                .convertAndSend(eq("test.exchange"), eq("test.routingKey"), captor.capture());

        Map<String, Object> payload = captor.getValue();
        assertEquals(showTime.getRoomId(), payload.get("roomId"));
        assertEquals(showTime.getPrice(), payload.get("price"));
        assertEquals(showTime.getMovieId(), payload.get("movieId"));
        assertEquals(showTime.getStartTime().toString(), payload.get("startTime"));
    }

    @Test
    void shouldThrowNotificationSendExceptionWhenAmqpFails() {
        // Arrange
        ShowTimeDomainEntity showTime = new ShowTimeDomainEntity(
                UUID.randomUUID(),
                new BigDecimal("50.00"),
                UUID.randomUUID(),
                LocalDateTime.of(2025, 11, 6, 20, 0)
        );

        doThrow(new RuntimeException("RabbitMQ error"))
                .when(amqpTemplate)
                .convertAndSend(anyString(), anyString(), (Object) any());

        // Act & Assert
        NotificationSendException ex = assertThrows(NotificationSendException.class,
                () -> publisher.publisherNewShowtimeCustomers(showTime));

        assertTrue(ex.getMessage().contains("Error enviando evento"));
        assertNotNull(ex.getCause());
        assertEquals("RabbitMQ error", ex.getCause().getMessage());
    }

}
