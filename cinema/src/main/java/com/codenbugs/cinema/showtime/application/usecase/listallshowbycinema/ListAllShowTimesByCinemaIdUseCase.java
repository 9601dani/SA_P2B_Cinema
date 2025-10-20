package com.codenbugs.cinema.showtime.application.usecase.listallshowbycinema;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.ports.input.ListAllShowTimesByCinemaIdInputPort;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class ListAllShowTimesByCinemaIdUseCase implements ListAllShowTimesByCinemaIdInputPort {

    private final FindingAllRoomsByCinemaIdOutputPort findingAllRoomsByCinemaIdOutputPort;
    private final ListAllShowTimesByListRoomsIdOutputPort listAllShowTimesByListRoomsIdOutputPort;

    @Override
    public List<ShowTimeDomainEntity> listAllShowTimesByCinemaId(UUID cinemaId) {
        // Obtener todas las salas del cine
        List<RoomDomainEntity> rooms = findingAllRoomsByCinemaIdOutputPort.findAllByCinemaId(cinemaId);

        // Crear un mapa (roomId -> nombreSala) para acceso rápido
        Map<UUID, String> roomNameById = rooms.stream()
                .collect(Collectors.toMap(RoomDomainEntity::getId, RoomDomainEntity::getName));

        // Obtener todas las funciones de esas salas
        List<UUID> roomsId = new ArrayList<>(roomNameById.keySet());
        List<ShowTimeDomainEntity> showTimes = listAllShowTimesByListRoomsIdOutputPort.findAllShowTimesByListRoomsId(roomsId);

        // Asignar el nombre de la sala a cada función
        showTimes.forEach(st -> st.setNameRoom(roomNameById.get(st.getRoomId())));

        return showTimes;
    }

}
