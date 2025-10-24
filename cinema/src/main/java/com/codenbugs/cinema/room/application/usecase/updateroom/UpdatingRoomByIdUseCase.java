package com.codenbugs.cinema.room.application.usecase.updateroom;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.room.application.ports.input.UpdatingRoomByIdInputPort;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByNameOutputPort;
import com.codenbugs.cinema.room.application.ports.output.UpdatingRoomByIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdatingRoomByIdUseCase implements UpdatingRoomByIdInputPort {

    private final UpdatingRoomByIdOutputPort updatingRoomByIdOutputPort;
    private final FindingRoomByIdOutputPort findingRoomByIdOutputPort;
    private final FindingRoomByNameOutputPort findingRoomByNameOutputPort;

    @Override
    @Transactional
    public RoomDomainEntity updatingRoomById(UUID roomId, UpdateRoomDto updateRoomDto) {

        RoomDomainEntity roomDomain = updateRoomDto.toDomain();

        // validaciones
        RoomDomainEntity currenRoom = findingRoomByIdOutputPort.findingRoomById(roomId)
                .orElseThrow(()-> new EntityNotFount("Sala no encontado para actualizar, Id: " + roomId));

        if (!currenRoom.getName().equalsIgnoreCase(updateRoomDto.getName()) &&
                findingRoomByNameOutputPort.findingRoomByNameAndCinemaId(roomDomain.getName(), currenRoom.getCinemaId()).isPresent()) {
            throw new EntityAlreadyExistsException("Ya existe un cine con ese nombre");
        }

        return updatingRoomByIdOutputPort.updatingRoomById(roomId, roomDomain);
    }
}
