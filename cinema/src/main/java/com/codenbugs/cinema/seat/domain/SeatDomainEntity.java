package com.codenbugs.cinema.seat.domain;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@DomainEntity
@Getter
public class SeatDomainEntity {
    private UUID id;
    private UUID roomId;
    private String name;
    private Integer rowNum;
    private Integer colNum;

    public SeatDomainEntity(UUID id, Integer colNum, Integer rowNum, String name, UUID roomId) {
        this.id = id;
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.name = name;
        this.roomId = roomId;
    }

    public SeatDomainEntity(Integer colNum, Integer rowNum, String name, UUID roomId) {
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.name = name;
        this.roomId = roomId;
    }
}
