package com.codenbugs.cinema.showtime.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.inputadapter.dto.RoomResponseDto;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.ReportShowTimesRoomsDto;
import com.codenbugs.cinema.showtime.infrastructure.inputadapter.dto.ShowTimeResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public ReportShowTimesRoomsDto toReportResponseDto(RoomDomainEntity roomEntity) {

        if (roomEntity == null) {
            return null;
        }

        List<ShowTimeResponseDto> showTimes = roomEntity.getShowTimes()
                .stream()
                .map(this::toResponseDto)
                .toList();

        return ReportShowTimesRoomsDto.builder()
                .id(roomEntity.getId())
                .name(roomEntity.getName())
                .description(roomEntity.getDescription())
                .rows(roomEntity.getRows())
                .columns(roomEntity.getColumns())
                .imageUrl(roomEntity.getImageUrl())
                .capacity(roomEntity.getCapacity())
                .showTimes(showTimes)
                .build();
    }
}
