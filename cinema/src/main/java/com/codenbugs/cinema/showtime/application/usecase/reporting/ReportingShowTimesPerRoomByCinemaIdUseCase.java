package com.codenbugs.cinema.showtime.application.usecase.reporting;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.input.ReportingShowTimesPerRoomByCinemaIdInputPort;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class ReportingShowTimesPerRoomByCinemaIdUseCase implements ReportingShowTimesPerRoomByCinemaIdInputPort {

    private final FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private final ListAllShowTimesByListRoomsIdOutputPort listAllShowTimesByListRoomsIdOutputPort;

    @Override
    public List<RoomDomainEntity> reportShowTimesPerRoomByCinemaId(UUID cinemaId) {
        // Obtener salas por cine
        List<RoomDomainEntity> rooms = findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId);

        if (rooms.isEmpty()) {
            return List.of();
        }

        // Obtener ids de salas
        List<UUID> roomsId = rooms.stream()
                .map(RoomDomainEntity::getId)
                .toList();

        // Obtener showtimes por salas
        List<ShowTimeDomainEntity> showTimes = listAllShowTimesByListRoomsIdOutputPort
                .findAllShowTimesByListRoomsId(roomsId);

        // Agrupar por roomId
        Map<UUID, List<ShowTimeDomainEntity>> showTimesByRoom = showTimes.stream()
                .collect(Collectors.groupingBy(ShowTimeDomainEntity::getRoomId));

        // Asignar a cada sala su lista de showtimes
        for (RoomDomainEntity room : rooms) {
            List<ShowTimeDomainEntity> list = showTimesByRoom.getOrDefault(room.getId(), List.of());
            room.setShowTimes(list);
        }

        return rooms;
    }

}
