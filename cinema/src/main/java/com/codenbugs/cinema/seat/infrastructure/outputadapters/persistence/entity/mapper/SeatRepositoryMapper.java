package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.mapper;

import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.SeatDbEntity;
import org.springframework.stereotype.Component;

@Component
public class SeatRepositoryMapper {

    public SeatDbEntity toDbEntity(SeatDomainEntity seatDomainEntity){
        if (seatDomainEntity == null){
            return null;
        }

        return SeatDbEntity.builder()
                .colNum(seatDomainEntity.getColNum())
                .rowNum(seatDomainEntity.getRowNum())
                .name(seatDomainEntity.getName())
                .roomId(seatDomainEntity.getRoomId())
                .build();
    }
    public SeatDomainEntity toSeatDomainEntity(SeatDbEntity seatDbEntity){

        if (seatDbEntity == null){
            return null;
        }

        return new SeatDomainEntity(
                seatDbEntity.getId(),
                seatDbEntity.getColNum(),
                seatDbEntity.getRowNum(),
                seatDbEntity.getName(),
                seatDbEntity.getRoomId());
    }
}
