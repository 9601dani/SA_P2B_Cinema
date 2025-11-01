package com.codenbugs.cinema.cinema.infrastructure.outputadapters.event;

import com.codenbugs.cinema.cinema.application.ports.output.CinemaCreateWalletEventPort;
import com.codenbugs.cinema.common.application.exception.NotificationSendException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class CreateWalletEventPublisher implements CinemaCreateWalletEventPort {

    private final AmqpTemplate amqpTemplate;

    private final String walletExchange;
    private final String walletCreatedRouting;

    public CreateWalletEventPublisher(AmqpTemplate amqpTemplate, Environment env) {
        this.amqpTemplate = amqpTemplate;
        this.walletExchange = env.getProperty("rabbitmq.exchange.wallet", "wallet.exchange");
        this.walletCreatedRouting = env.getProperty("rabbitmq.routing.wallet_created", "wallet.created");
    }


    @Override
    public void publishCinemaCreateWallet(UUID cinemaId) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("ownerId", cinemaId);
            payload.put("ownerType", "cinema");
            amqpTemplate.convertAndSend(walletExchange, walletCreatedRouting, payload);
        } catch (Exception e) {
            throw new NotificationSendException("Error enviando evento de creación de wallet", e);
        }
    }
}
