package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.mapper;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import org.springframework.stereotype.Component;

@Component
public class ShowTimeRepositoryMapper {
    public ShowTimeDomainEntity toDomainEntity(ShowTimeDbEntity showTimeDbEntity){
        if (showTimeDbEntity == null){
            return null;
        }

        return new ShowTimeDomainEntity(showTimeDbEntity.getId(),
                showTimeDbEntity.getRoomId(),
                showTimeDbEntity.getPrice(),
                showTimeDbEntity.getMovieId(),
                showTimeDbEntity.getStartTime(),
                showTimeDbEntity.getEndTime(),
                showTimeDbEntity.isActive());
    }

    public ShowTimeDbEntity timeDbEntity(ShowTimeDomainEntity showTimeDomainEntity){
        if (showTimeDomainEntity == null){
            return null;
        }

        return ShowTimeDbEntity.builder()
                .roomId(showTimeDomainEntity.getRoomId())
                .movieId(showTimeDomainEntity.getMovieId())
                .price(showTimeDomainEntity.getPrice())
                .startTime(showTimeDomainEntity.getStartTime())
                .endTime(showTimeDomainEntity.getEndTime())
                .active(showTimeDomainEntity.getActive())
                .build();
    }
}
