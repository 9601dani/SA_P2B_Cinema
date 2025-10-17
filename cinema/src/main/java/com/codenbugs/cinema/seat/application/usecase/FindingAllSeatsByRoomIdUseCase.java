package com.codenbugs.cinema.seat.application.usecase;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.seat.application.ports.input.FindingAllSeatsByRoomIdInputPort;
import com.codenbugs.cinema.seat.application.ports.output.FindingAllSeatsByRoomIdOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindingAllSeatsByRoomIdUseCase implements FindingAllSeatsByRoomIdInputPort {

    private final FindingAllSeatsByRoomIdOutputPort findingAllSeatsByRoomIdOutputPort;

    @Override
    @Transactional(readOnly = true)
    public List<SeatDomainEntity> findAllSeatsByRoomId(UUID roomId) {
        return findingAllSeatsByRoomIdOutputPort.findAllSeatsByRoomId(roomId);
    }
}
