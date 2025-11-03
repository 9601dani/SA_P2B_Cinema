package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.ticketsale.application.usecase.update.UpdatingSeatCaseDto;

import java.util.UUID;

public interface UpdatingSeatByTicketIdInputPort {
    void updateSeatByTicketId(UpdatingSeatCaseDto command, UUID ticketId);
}
