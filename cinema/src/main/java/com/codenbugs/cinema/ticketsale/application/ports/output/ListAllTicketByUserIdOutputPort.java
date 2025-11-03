package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllTicketByUserIdOutputPort {
    List<TicketSaleDomainEntity> listAllTicketsByUserId(UUID userId);
}
