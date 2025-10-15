package com.codenbugs.cinema.room.application.ports.input;

import com.codenbugs.cinema.room.application.usecase.createroom.CreateRoomDto;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import jakarta.validation.Valid;

public interface CreatingRoomInputPort {
    RoomDomainEntity createRoom(@Valid CreateRoomDto createRoomDto);
}
