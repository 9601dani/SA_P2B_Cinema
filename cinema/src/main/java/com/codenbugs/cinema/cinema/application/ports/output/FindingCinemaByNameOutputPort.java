package com.codenbugs.cinema.cinema.application.ports.output;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.Optional;

public interface FindingCinemaByNameOutputPort {
    Optional<CinemaDomainEntity> findCinemaByName(String name);
}
