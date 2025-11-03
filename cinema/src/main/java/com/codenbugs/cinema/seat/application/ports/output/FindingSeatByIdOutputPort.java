package com.codenbugs.cinema.seat.application.ports.output;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingSeatByIdOutputPort {
    Optional<SeatDomainEntity> findById(UUID seatId);
}
