package com.codenbugs.cinema.showtime.application.ports.output;

import java.time.LocalDateTime;
import java.util.UUID;

public interface FindingShowTimeRangeDateByRoomIdOutputPort {
    boolean existRegistersRangeDate(UUID roomId, LocalDateTime startDate, LocalDateTime endDate);
}
