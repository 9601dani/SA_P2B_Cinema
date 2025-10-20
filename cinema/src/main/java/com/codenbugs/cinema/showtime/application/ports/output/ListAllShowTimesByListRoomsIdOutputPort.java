package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllShowTimesByListRoomsIdOutputPort {
    List<ShowTimeDomainEntity> findAllShowTimesByListRoomsId(List<UUID> roomsId);
}
