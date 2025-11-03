package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.event;


import com.codenbugs.cinema.ticketsale.application.ports.input.UpdatingStateByIdInputPort;
import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdateStateCaseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateStateEventListener {


    private final UpdatingStateByIdInputPort updatingStateByIdInputPort;


    @RabbitListener(queues = "${rabbitmq.queues.stateTicket_created_notify}")
    public void handleNewShowTime(Map<String, Object> payload) {
        try {
            var ticketId = payload.get("ticketId").toString();
            var state = payload.get("state").toString();
            boolean createEventPayment = Boolean.parseBoolean(payload.get("createEventPayment").toString());

            var dto = new UpdateStateCaseDto(
                    UUID.fromString(ticketId),
                    state,
                    UUID.fromString(payload.get("walletId").toString())
            );

            updatingStateByIdInputPort.updateStateById(dto, createEventPayment);
        }catch (Exception e) {
            // TODO: manejar el error adecuadamente
        }
    }
}
