package com.codenbugs.cinema.showtime.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@DomainEntity
@Getter
public class MovieDomainEntity {
    private final UUID id;
    private final String title;
    private final Integer durationMinutes;
    private final String posterUrl;
    private final List<CategoryDomainEntity> categories;
    private final boolean active;


    public MovieDomainEntity(UUID id, String title, Integer durationMinutes, String posterUrl, List<CategoryDomainEntity> categories, boolean active) {
        this.id = id;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.posterUrl = posterUrl;
        this.categories = categories;
        this.active = active;
    }

    public void validateActive() {
        if (!active) {
            throw new InvalidPropertyEntityDomain("La pelicula ya no esta disponible para crear la funcion.");
        }
    }
}
