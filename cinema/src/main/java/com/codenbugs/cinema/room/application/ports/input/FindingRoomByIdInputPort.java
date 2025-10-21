package com.codenbugs.cinema.room.application.ports.input;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.UUID;

public interface FindingRoomByIdInputPort {
    RoomDomainEntity findRoomById(UUID id);
}
