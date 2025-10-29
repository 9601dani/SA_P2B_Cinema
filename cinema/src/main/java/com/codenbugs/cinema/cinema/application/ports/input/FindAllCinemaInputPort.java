package com.codenbugs.cinema.cinema.application.ports.input;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.List;

public interface FindAllCinemaInputPort {
    List<CinemaDomainEntity> findAll();
}
