package com.codenbugs.cinema.showtime.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.ShowTimeResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ShowTimeRestMapper {

    public ShowTimeResponseDto toResponseDto(ShowTimeDomainEntity domain){
        if(domain == null){
            return null;
        }

        return ShowTimeResponseDto.builder()
                .id(domain.getId())
                .roomId(domain.getRoomId())
                .price(domain.getPrice())
                .movieId(domain.getMovieId())
                .startTime(domain.getStartTime())
                .endTime(domain.getEndTime())
                .active(domain.getActive())
                .durationMinutes(domain.getDurationMinutes())
                .nameRoom(domain.getNameRoom())
                .build();

    }
}
