package com.codenbugs.cinema.showtime.application.usecase.createshowtime;

import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Value
@AllArgsConstructor
public class CreateShowTimeCaseDto {
    UUID movieId;
    BigDecimal price;
    UUID roomId;
    LocalDate startDate;
    LocalTime startTime;

    public ShowTimeDomainEntity toDomain() {
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        return new ShowTimeDomainEntity(roomId, price, movieId, startDateTime);
    }

}
