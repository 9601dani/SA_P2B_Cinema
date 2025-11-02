package com.codenbugs.cinema.showtime.application.ports.input;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.showtime.application.usecase.reporting.ReportingRangeDto;

import java.util.List;

public interface ReportingShowTimesPerRoomByCinemaIdRangeDateInputPort {
    List<RoomDomainEntity> reportShowTimesPerRoomByCinemaIdRangeDate(ReportingRangeDto reportingRangeDto);
}
