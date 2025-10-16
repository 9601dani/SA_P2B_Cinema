package com.codenbugs.cinema.room.application.ports.input;

import com.codenbugs.cinema.room.application.usecase.updateroom.UpdateRoomDto;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.UUID;

public interface UpdatingRoomByIdInputPort {
    RoomDomainEntity updatingRoomById(UUID roomId, UpdateRoomDto updateRoomDto);
}
