package com.codenbugs.cinema.showtimeSeat.domain;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@DomainEntity
@Getter
public class ShowTimeSeatDomainEntity {
    private UUID id;
    private UUID seatId;
    private UUID showTimeId;
    private ShowtimeSeatStatus status;
}
