package com.codenbugs.cinema.room.application.ports.output;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.UUID;

public interface UpdatingRoomByIdOutputPort {
    RoomDomainEntity updatingRoomById(UUID roomId, RoomDomainEntity roomDomain);
}
