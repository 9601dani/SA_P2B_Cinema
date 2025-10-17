package com.codenbugs.cinema.seat.application.ports.input;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;

import java.util.List;
import java.util.UUID;

public interface FindingAllSeatsByRoomIdInputPort {
    List<SeatDomainEntity> findAllSeatsByRoomId(UUID roomId);
}
