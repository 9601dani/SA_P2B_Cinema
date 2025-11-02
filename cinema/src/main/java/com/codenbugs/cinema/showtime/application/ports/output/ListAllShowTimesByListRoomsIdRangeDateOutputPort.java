package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ListAllShowTimesByListRoomsIdRangeDateOutputPort {
    List<ShowTimeDomainEntity> findAllShowTimesByListRoomsIdRangeDate(List<UUID> roomsId,
                                                                      LocalDateTime startTime,
                                                                      LocalDateTime endTime);

}
