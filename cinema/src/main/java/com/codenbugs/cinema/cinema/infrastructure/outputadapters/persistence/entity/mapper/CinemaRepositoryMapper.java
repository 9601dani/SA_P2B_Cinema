package com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.mapper;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.CinemaDbEntity;
import org.springframework.stereotype.Component;

@Component
public class CinemaRepositoryMapper {

    public CinemaDbEntity toDbEntity(CinemaDomainEntity cinemaDomainEntity) {
        if (cinemaDomainEntity == null) {
            return null;
        }

        return CinemaDbEntity.builder()
                .name(cinemaDomainEntity.getName())
                .address(cinemaDomainEntity.getAddress())
                .adminUserId(cinemaDomainEntity.getAdminUserId())
                .dailyCost(cinemaDomainEntity.getDailyCost())
                .imageUrl(cinemaDomainEntity.getImageUrl())
                .build();

    }

    public CinemaDomainEntity toDomainEntity(CinemaDbEntity cinemaDbEntity){
        if (cinemaDbEntity == null) {
            return null;
        }

        return new CinemaDomainEntity(cinemaDbEntity.getId(),
                cinemaDbEntity.getName(),
                cinemaDbEntity.getImageUrl(),
                cinemaDbEntity.getAddress(),
                cinemaDbEntity.getAdminUserId(),
                cinemaDbEntity.getDailyCost(),
                cinemaDbEntity.getCreatedAt());
    }
}
