package com.codenbugs.cinema.showtime.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@DomainEntity
@Getter
public class CategoryDomainEntity {
    private final UUID id;
    private final String name;

    public CategoryDomainEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
