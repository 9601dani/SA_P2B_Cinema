package com.codenbugs.cinema.showtime.infrastructure.outputadapters.event;

import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import com.codenbugs.cinema.showtime.application.ports.output.NotificationCreatedShowTimeEventPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationNewShowTimeEventPublisher implements NotificationCreatedShowTimeEventPort {

    private final AmqpTemplate amqpTemplate;
    private final String exchange;
    private final String routingKey;

    public NotificationNewShowTimeEventPublisher(AmqpTemplate amqpTemplate, Environment env) {
        this.amqpTemplate = amqpTemplate;
        // Aqui se declara la configuración que usamos en el YAML o que colocamos ahí
        this.exchange = env.getProperty("rabbitmq.exchange", "showTime.exchange");
        this.routingKey = env.getProperty("rabbitmq.routing.showTime_created", "showTime.created");
    }

    @Override
    public void publisherNewShowtimeCustomers(ShowTimeDomainEntity domain) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("roomId", domain.getRoomId());
            payload.put("price", domain.getPrice());
            payload.put("movieId", domain.getMovieId());
            payload.put("startTime", domain.getStartTime().toString());

            amqpTemplate.convertAndSend(exchange, routingKey, payload);
        } catch (Exception e) {
            throw new NotificationSendException("Error enviando evento de registro a RabbitMQ", e);
        }
    }
}
