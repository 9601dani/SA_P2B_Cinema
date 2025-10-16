package com.codenbugs.cinema.room.application.usecase.updateroom;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UpdateRoomDto {
    String name;
    String description;
    String imageUrl;
    boolean commentsEnabled;
    boolean blocked;

    public RoomDomainEntity toDomain(){
        return new RoomDomainEntity(name, description, imageUrl, commentsEnabled, blocked);
    }
}
