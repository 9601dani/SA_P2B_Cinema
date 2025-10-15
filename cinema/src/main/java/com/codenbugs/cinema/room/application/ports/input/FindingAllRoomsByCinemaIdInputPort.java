package com.codenbugs.cinema.room.application.ports.input;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.List;
import java.util.UUID;

public interface FindingAllRoomsByCinemaIdInputPort {
    List<RoomDomainEntity> findAllByCinemaId(UUID cinemaId);
}
