package com.codenbugs.cinema.room.application.ports.output;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.List;
import java.util.UUID;

public interface FindingAllRoomsByCinemaIdOutputPort {
    List<RoomDomainEntity> findAllByCinemaId(UUID cinemaId);
}
