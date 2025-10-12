package com.codenbugs.cinema.cinema.application.ports.output;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingCinemaByIdAdminOutputPort {
    Optional<CinemaDomainEntity> findCinemaByIdAdmin(UUID id);
}
