package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.mapper;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.RoomDbEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomRepositoryMapper{

    public RoomDbEntity toDbEntity(RoomDomainEntity roomDomainEntity) {
        if (roomDomainEntity == null) {
            return null;
        }

        return RoomDbEntity.builder()
                .id(roomDomainEntity.getId())
                .name(roomDomainEntity.getName())
                .description(roomDomainEntity.getDescription())
                .imageUrl(roomDomainEntity.getImageUrl())
                .rows(roomDomainEntity.getRows())
                .columns(roomDomainEntity.getColumns())
                .capacity(roomDomainEntity.getCapacity())
                .commentsEnabled(roomDomainEntity.isCommentsEnabled())
                .blocked(roomDomainEntity.isBlocked())
                .cinemaId(roomDomainEntity.getCinemaId())
                .build();
    }

    public RoomDomainEntity toDomainEntity(RoomDbEntity roomDbEntity){
       if (roomDbEntity == null) {
           return null;
       }

       return new RoomDomainEntity(roomDbEntity.getId(),
               roomDbEntity.getCinemaId(),
               roomDbEntity.getCapacity(),
               roomDbEntity.getImageUrl(),
               roomDbEntity.getName(),
               roomDbEntity.getRows(),
               roomDbEntity.getColumns(),
               roomDbEntity.getDescription(),
               roomDbEntity.isCommentsEnabled(),
               roomDbEntity.isBlocked());
    }
}
