package com.codenbugs.cinema.room.domain;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@DomainEntity
@Getter
public class RoomDomainEntity {
    private UUID id;
    private UUID cinemaId;
    private Integer capacity;
    private String imageUrl;
    private String name;
    private Integer rows;
    private Integer columns;
    private String description;
    private boolean commentsEnabled;
    private boolean blocked;

}
