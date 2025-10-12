package com.codenbugs.cinema.showtime.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@DomainEntity
@Getter
public class ShowTimeDomainEntity {
    private UUID id;
    private UUID roomId;
    private UUID movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean active;
}
