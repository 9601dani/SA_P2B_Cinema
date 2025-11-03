package com.codenbugs.cinema.ticketsale.application.usecase.update;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.seat.application.ports.output.FindingSeatByIdOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.ticketsale.application.ports.input.UpdatingSeatByTicketIdInputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketByIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingTicketBySeatIdAndShowTimeIdOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.UpdatingSeatByTicketIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdatingSeatByTicketIdUseCase implements UpdatingSeatByTicketIdInputPort {

    private final UpdatingSeatByTicketIdOutputPort updatingSeatByTicketIdOutputPort;
    private final FindingSeatByIdOutputPort findingSeatByIdOutputPort;
    private final FindingTicketByIdOutputPort findingTicketByIdOutputPort;
    private final FindingTicketBySeatIdAndShowTimeIdOutputPort findingTicketBySeatIdAndShowTimeIdOutputPort;


    @Override
    @Transactional
    public void updateSeatByTicketId(UpdatingSeatCaseDto command, UUID ticketId) {
        TicketSaleDomainEntity ticket = findingTicketByIdOutputPort.findingTicketById(ticketId)
                .orElseThrow(() -> new EntityNotFount("Ticket no encontrado para actualizar el asieto."));

        // validar que el asiento exista y que no este ocupado en esa funcion
        SeatDomainEntity seat = findingSeatByIdOutputPort.findById(command.getSeatId())
                .orElseThrow(() -> new EntityNotFount("Asiento no encontrado para la actualizacion de ticket."));

        TicketSaleDomainEntity existingTicket = findingTicketBySeatIdAndShowTimeIdOutputPort
                .findBySeatIdAndShowTimeId(seat.getId(), ticket.getShowtimeId())
                .orElse(null);

        if (existingTicket != null && existingTicket.getState() != StateTicket.REJECTED) {
            throw new EntityAlreadyExistsException("No se puede actualizar el asiento, ya se encuentra reservado.");
        }

        updatingSeatByTicketIdOutputPort.updateSeatByTicketIdInDatabase(command.getSeatId(), ticketId);
    }
}
