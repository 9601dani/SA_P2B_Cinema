package com.codenbugs.cinema.showtime.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@DomainEntity
@Getter
public class ShowTimeDomainEntity {
    private UUID id;
    private UUID roomId;
    private BigDecimal price;
    private UUID movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean active;
    private Integer durationMinutes;
    @Setter
    private String nameRoom;

    public ShowTimeDomainEntity(UUID id, UUID roomId, BigDecimal price, UUID movieId, LocalDateTime startTime, LocalDateTime endTime, Boolean active) {
        this.id = id;
        this.roomId = roomId;
        this.price = price;
        this.movieId = movieId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
        this.calculateDurationMinutes();
    }

    public ShowTimeDomainEntity(UUID roomId, BigDecimal price, UUID movieId, LocalDateTime startTime) {
        this.roomId = roomId;
        this.price = price;
        this.movieId = movieId;
        this.startTime = startTime;
        this.active = true;
        this.validate();
    }

    public void calculateEndTime(Integer durationMinutes) {
        if (durationMinutes == null || durationMinutes <= 0) {
            throw new InvalidPropertyEntityDomain("La duración debe ser mayor a cero");
        }

        this.endTime = startTime.plusMinutes(durationMinutes);
    }

    private void validate(){
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPropertyEntityDomain("El precio debe ser mayor a cero");
        }

        if (roomId == null) {
            throw new InvalidPropertyEntityDomain("El id de sala es obligatorio");
        }

        if (movieId == null) {
            throw new InvalidPropertyEntityDomain("El id de pelicual es obligatorio");
        }

        if (startTime == null) {
            throw new InvalidPropertyEntityDomain("La hora de inicio no puede ser nula");
        }
    }

    public void validateCurrentDate() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = startTime.toLocalDate();

        if (startDate.isBefore(today)) {
            throw new InvalidPropertyEntityDomain("La fecha de inicio no puede ser menor a la fecha actual.");
        }
    }

    // endTime - startTime
    private void calculateDurationMinutes(){
        long minutes = Duration.between(startTime, endTime).toMinutes();
        this.durationMinutes = (int) minutes;
    }

}
