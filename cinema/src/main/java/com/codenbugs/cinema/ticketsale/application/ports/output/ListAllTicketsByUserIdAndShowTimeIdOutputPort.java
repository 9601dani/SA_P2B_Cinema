package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllTicketsByUserIdAndShowTimeIdOutputPort {
    List<TicketSaleDomainEntity> listAllTicketsByUserIdAndShowTimeId(UUID userId, UUID showTimeId);
}
