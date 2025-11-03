package com.codenbugs.cinema.ticketsale.application.ports.input;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllTicketByUserIdInputPort {
    List<TicketSaleDomainEntity> listAllTicketsByUserId(UUID userId);
}
