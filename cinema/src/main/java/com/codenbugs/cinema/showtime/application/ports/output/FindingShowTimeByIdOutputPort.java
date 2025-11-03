package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

import java.util.Optional;
import java.util.UUID;

public interface FindingShowTimeByIdOutputPort {
    Optional<ShowTimeDomainEntity> findById(UUID id);
}
