package com.codenbugs.cinema.room.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.RoomResponseDto;
import org.springframework.stereotype.Component;

@Component
public class RoomRestMapper {

    public RoomResponseDto  toRoomResponseDto(RoomDomainEntity roomEntity) {
        if (roomEntity == null) {
            return null;
        }

        return RoomResponseDto.builder()
                .id(roomEntity.getId())
                .name(roomEntity.getName())
                .description(roomEntity.getDescription())
                .commentsEnabled(roomEntity.isCommentsEnabled())
                .rows(roomEntity.getRows())
                .columns(roomEntity.getColumns())
                .imageUrl(roomEntity.getImageUrl())
                .blocked(roomEntity.isBlocked())
                .cinemaId(roomEntity.getCinemaId())
                .capacity(roomEntity.getCapacity())
                .build();
    }
}
