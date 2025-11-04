package com.codenbugs.cinema.seat.application.ports.output;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;

import java.util.List;
import java.util.UUID;

public interface FindingAllSeatsByListRoomIdsOutputPort {
    List<SeatDomainEntity> findAllSeatsByListRoomIds(List<UUID> roomIds);
}
