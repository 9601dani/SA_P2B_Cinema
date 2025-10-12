package com.codenbugs.cinema.seat.domain;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@DomainEntity
@Getter
public class SeatDomainEntity {
    private UUID id;
    private UUID roomId;
    private Integer rowNum;
    private Integer colNum;
}
