package com.codenbugs.cinema.room.application.ports.output;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.Optional;

public interface FindingRoomByNameOutputPort {
    Optional<RoomDomainEntity> findingRoomByName(String name);
}
