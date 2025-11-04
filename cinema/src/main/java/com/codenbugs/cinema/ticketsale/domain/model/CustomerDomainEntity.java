package com.codenbugs.cinema.ticketsale.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
@DomainEntity
public class CustomerDomainEntity {
    private UUID userId;
    private String fullName;

    public CustomerDomainEntity(UUID userId, String fullName) {
        this.userId = userId;
        this.fullName = fullName;
    }
}
