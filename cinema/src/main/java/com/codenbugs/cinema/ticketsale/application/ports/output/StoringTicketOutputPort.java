package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.UUID;

public interface StoringTicketOutputPort {
    UUID save(TicketSaleDomainEntity domainEntity);
}
