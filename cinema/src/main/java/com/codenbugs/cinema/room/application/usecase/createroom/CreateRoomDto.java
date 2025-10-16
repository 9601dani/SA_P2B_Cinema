package com.codenbugs.cinema.room.application.usecase.createroom;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class CreateRoomDto {
    UUID cinemaId;
    String name;
    String description;
    Integer rows;
    Integer columns;
    String imageUrl;

    public RoomDomainEntity toDomain() {
        if (this.name == null) {
            return null;
        }
        return new RoomDomainEntity(this.cinemaId,
               this.imageUrl,
                this.name,
                this.rows,
                this.columns,
                this.description
                );
    }
}
