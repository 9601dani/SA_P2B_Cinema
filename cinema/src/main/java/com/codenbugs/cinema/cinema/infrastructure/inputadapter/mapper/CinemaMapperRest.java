package com.codenbugs.cinema.cinema.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.inputadapter.dto.CinemaResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapperRest {

    public CinemaResponseDto toResponseDto(CinemaDomainEntity cinemaDomainEntity) {
        return CinemaResponseDto.builder()
                .id(cinemaDomainEntity.getId())
                .name(cinemaDomainEntity.getName())
                .dailyCost(cinemaDomainEntity.getDailyCost())
                .address(cinemaDomainEntity.getAddress())
                .adminUserId(cinemaDomainEntity.getAdminUserId())
                .imageUrl(cinemaDomainEntity.getImageUrl())
                .createdAt(cinemaDomainEntity.getCreatedAt())
                .build();
    }
}
