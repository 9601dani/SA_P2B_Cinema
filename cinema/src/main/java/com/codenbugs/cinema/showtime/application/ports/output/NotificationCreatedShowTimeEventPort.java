package com.codenbugs.cinema.showtime.application.ports.output;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;

public interface NotificationCreatedShowTimeEventPort {
    void publisherNewShowtimeCustomers( ShowTimeDomainEntity domain );
}
