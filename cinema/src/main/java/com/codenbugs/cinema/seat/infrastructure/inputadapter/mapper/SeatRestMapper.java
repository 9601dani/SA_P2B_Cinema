package com.codenbugs.cinema.seat.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.inputadapter.dto.SeatResponseDto;
import org.springframework.stereotype.Component;

@Component
public class SeatRestMapper {

    public SeatResponseDto responseDto(SeatDomainEntity domain){
        if(domain == null){
            return null;
        }

        return SeatResponseDto.builder()
                .id(domain.getId())
                .name(domain.getName())
                .roomId(domain.getRoomId())
                .colNum(domain.getColNum())
                .rowNum(domain.getRowNum())
                .build();
    }
}
