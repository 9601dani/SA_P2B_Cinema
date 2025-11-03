package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingTicketByIdOutputPort {
    Optional<TicketSaleDomainEntity> findingTicketById(UUID id);
}
