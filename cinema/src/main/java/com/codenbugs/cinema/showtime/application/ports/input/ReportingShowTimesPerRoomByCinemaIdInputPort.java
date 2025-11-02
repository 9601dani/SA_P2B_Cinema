package com.codenbugs.cinema.showtime.application.ports.input;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ReportingShowTimesPerRoomByCinemaIdInputPort {
    List<RoomDomainEntity> reportShowTimesPerRoomByCinemaId(UUID cinemaId);
}
