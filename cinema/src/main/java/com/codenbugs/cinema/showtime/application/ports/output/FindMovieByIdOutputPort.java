package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;

import java.util.UUID;

public interface FindMovieByIdOutputPort {
    MovieDomainEntity findById(UUID id);
}
