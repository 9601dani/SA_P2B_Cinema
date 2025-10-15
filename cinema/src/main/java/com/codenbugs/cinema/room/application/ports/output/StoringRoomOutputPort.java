package com.codenbugs.cinema.room.application.ports.output;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

public interface StoringRoomOutputPort {
    RoomDomainEntity save(RoomDomainEntity roomDomain);
}
