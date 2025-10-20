package com.codenbugs.cinema.showtime.application.ports.input;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

import java.util.List;
import java.util.UUID;

public interface ListAllShowTimesByCinemaIdInputPort {
    List<ShowTimeDomainEntity> listAllShowTimesByCinemaId(UUID cinemaId);
}
