package com.codenbugs.cinema.cinema.application.ports.output;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.List;
import java.util.Optional;

public interface FindingAllCinemaOutputPort {
    List<CinemaDomainEntity> findAllCinemas();
}
