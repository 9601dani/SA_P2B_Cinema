package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;

import java.util.List;

public interface FindingAllMoviesOutputPort {
    List<MovieDomainEntity> findAllMoviesByList();
}
