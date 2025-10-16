package com.codenbugs.cinema.room.application.ports.output;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingRoomByIdOutputPort {
    Optional<RoomDomainEntity> findingRoomById(UUID roomId);
}
