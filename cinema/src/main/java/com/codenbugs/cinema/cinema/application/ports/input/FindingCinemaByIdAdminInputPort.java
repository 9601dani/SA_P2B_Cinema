package com.codenbugs.cinema.cinema.application.ports.input;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.UUID;

public interface FindingCinemaByIdAdminInputPort {
    CinemaDomainEntity  findCinemaByIdAdmin(UUID id);
}
