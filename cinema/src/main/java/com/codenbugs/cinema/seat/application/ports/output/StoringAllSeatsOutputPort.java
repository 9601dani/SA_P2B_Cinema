package com.codenbugs.cinema.seat.application.ports.output;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;

import java.util.List;

public interface StoringAllSeatsOutputPort {
    void saveAll(List<SeatDomainEntity> seats);
}
