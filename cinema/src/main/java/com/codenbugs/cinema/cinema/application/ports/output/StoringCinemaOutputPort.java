package com.codenbugs.cinema.cinema.application.ports.output;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

public interface StoringCinemaOutputPort {
    CinemaDomainEntity save(CinemaDomainEntity cinema);
}
