package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

public interface StoringShowTimeOutputPort {
    void save(ShowTimeDomainEntity showTimeDomainEntity);
}
