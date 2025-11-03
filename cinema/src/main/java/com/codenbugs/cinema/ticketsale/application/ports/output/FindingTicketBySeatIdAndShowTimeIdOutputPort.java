package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingTicketBySeatIdAndShowTimeIdOutputPort {
    Optional<TicketSaleDomainEntity> findBySeatIdAndShowTimeId(
            UUID seatId,
            UUID showTimeId
    );
}
