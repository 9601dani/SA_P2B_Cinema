package com.codenbugs.cinema.room.application.usecase.findcinema;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.room.application.ports.input.FindingAllRoomsByCinemaIdInputPort;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindingAllRoomsUseCase implements FindingAllRoomsByCinemaIdInputPort {

    private final FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;

    @Override
    @Transactional(readOnly = true)
    public List<RoomDomainEntity> findAllByCinemaId(UUID cinemaId) {
        return findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId);
    }
}
