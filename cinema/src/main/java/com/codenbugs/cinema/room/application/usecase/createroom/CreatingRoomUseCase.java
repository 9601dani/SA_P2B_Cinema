package com.codenbugs.cinema.room.application.usecase.createroom;

import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.input.CreatingRoomInputPort;
import com.codenbugs.cinema.room.application.ports.output.StoringRoomOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.seat.application.ports.output.StoringAllSeatsOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreatingRoomUseCase implements CreatingRoomInputPort {

    private final FindingCinemaByIdOutputPort findingCinemaByIdOutputPort;
    private final StoringRoomOutputPort  storingRoomOutputPort;
    private final StoringAllSeatsOutputPort storingAllSeatsOutputPort;

    @Override
    @Transactional
    public RoomDomainEntity createRoom(CreateRoomDto createRoomDto) {

        RoomDomainEntity roomDomain = createRoomDto.toDomain();

        // validar existe cinema id
        if (findingCinemaByIdOutputPort.findCinemaById(roomDomain.getCinemaId()).isEmpty()){
            throw new EntityNotFount("Cine no encontrado con id: " + roomDomain.getCinemaId());
        }

        //calculos de dominio
        roomDomain.creationCalculations();

        RoomDomainEntity savedRoom = storingRoomOutputPort.save(roomDomain);

        // creat seats
        List<SeatDomainEntity> seats = savedRoom.generateSeats();
        storingAllSeatsOutputPort.saveAll(seats);

        return savedRoom;
    }
}
