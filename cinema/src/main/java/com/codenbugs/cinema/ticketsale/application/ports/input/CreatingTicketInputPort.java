package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.ticketsale.application.usecase.created.CreateTicketSaleCaseDto;

public interface CreatingTicketInputPort {
    void createTicket(CreateTicketSaleCaseDto command);
}
