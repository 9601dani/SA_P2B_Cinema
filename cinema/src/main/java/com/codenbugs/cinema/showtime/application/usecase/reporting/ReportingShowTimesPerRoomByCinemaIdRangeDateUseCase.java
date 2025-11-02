package com.codenbugs.cinema.showtime.application.usecase.reporting;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.input.ReportingShowTimesPerRoomByCinemaIdRangeDateInputPort;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdRangeDateOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class ReportingShowTimesPerRoomByCinemaIdRangeDateUseCase implements ReportingShowTimesPerRoomByCinemaIdRangeDateInputPort {

    private final FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private final ListAllShowTimesByListRoomsIdRangeDateOutputPort listAllShowTimesByListRoomsIdRangeDateOutputPort;


    @Override
    public List<RoomDomainEntity> reportShowTimesPerRoomByCinemaIdRangeDate(ReportingRangeDto reportingRangeDto) {

        // Obtener salas por cine
        List<RoomDomainEntity> rooms = findingAllRoomsByCinemaIdOutputPort
                .findAllByCinemaId(reportingRangeDto.getCinemaId());

        if (rooms.isEmpty()) {
            return List.of();
        }

        // Obtener ids de salas
        List<UUID> roomsId = rooms.stream()
                .map(RoomDomainEntity::getId)
                .toList();

        // convertir localdate a date LocalDateTime
        LocalDateTime startDateTime = reportingRangeDto.getStartDate().atStartOfDay();
        LocalDateTime endDateTime = reportingRangeDto.getEndDate().atStartOfDay();

        // Obtener showtimes por salas
        List<ShowTimeDomainEntity> showTimes = listAllShowTimesByListRoomsIdRangeDateOutputPort
                .findAllShowTimesByListRoomsIdRangeDate(roomsId, startDateTime, endDateTime);

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
