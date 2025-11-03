package com.codenbugs.cinema.ticketsale.application.usecase.update;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.ticketsale.application.ports.input.UpdatingStateByIdInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketByIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.PayTicketEventPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.UpdatingStateByIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdatingStateByIdUseCase implements UpdatingStateByIdInputPort {

    private final UpdatingStateByIdOutputPort updatingStateByIdOutputPort;
    private final FindingTicketByIdOutputPort findingTicketByIdOutputPort;
    private final PayTicketEventPort payTicketEventPort;

    @Override
    @Transactional
    public void updateStateById(UpdateStateCaseDto dto, boolean createEventPayment) {
        var ticket = findingTicketByIdOutputPort.findingTicketById(dto.getId())
                .orElseThrow(() -> new EntityNotFount("Ticket no encontrado para actualizar su estado."));

        var newState = dto.convertTargetType();

        if (ticket.getState() == newState) {
            return;
        }

        if (newState == StateTicket.COMPLETED_PAYMENT && createEventPayment) {
            ticket.setWalletId(dto.getWalletId());
            payTicketEventPort.payTicketEvent(ticket, dto.getId());
            return;
        }

        updatingStateByIdOutputPort.updateStateById(dto.getId(), newState);

    }
}
