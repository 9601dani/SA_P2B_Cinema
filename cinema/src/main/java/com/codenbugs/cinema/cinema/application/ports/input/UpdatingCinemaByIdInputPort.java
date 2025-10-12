package com.codenbugs.cinema.cinema.application.ports.input;

import com.codenbugs.cinema.cinema.application.usecase.updatecinema.UpdateCinemaDto;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;

import java.util.UUID;

public interface UpdatingCinemaByIdInputPort {
    CinemaDomainEntity updatingCinemaById(UUID cinemaId, UpdateCinemaDto updateCinemaDto);
}
