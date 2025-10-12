package com.codenbugs.cinema.cinema.application.ports.output;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.UUID;

public interface UpdatingCinemaByIdOutputPort {
    CinemaDomainEntity update(UUID cinemaId, CinemaDomainEntity cinema);
}
