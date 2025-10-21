package com.codenbugs.cinema.room.application.usecase.findroom;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.input.FindingRoomByIdInputPort;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindingRoomByIdUseCase implements FindingRoomByIdInputPort {

    private final FindingRoomByIdOutputPort findingRoomByIdOutputPort;

    @Override
    public RoomDomainEntity findRoomById(UUID id) {
        return findingRoomByIdOutputPort.findingRoomById(id)
                .orElseThrow(() -> new EntityNotFount("Sala no encontrada con el id"+ id));
    }
}
