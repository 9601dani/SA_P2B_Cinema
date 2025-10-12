package com.codenbugs.cinema.cinema.application.ports.input;

import com.codenbugs.cinema.cinema.application.usecase.createcinema.CreateCinemaDto;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import jakarta.validation.Valid;

public interface CreatingCinemaInputPort {
    CinemaDomainEntity createCinema(@Valid CreateCinemaDto createCinemaDto);
}
