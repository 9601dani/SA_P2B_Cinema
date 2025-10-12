package com.codenbugs.cinema.cinema.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@DomainEntity
@Getter
public class CinemaDomainEntity {
    private UUID id;
    private String name;
    private String imageUrl;
    private String address;
    private UUID adminUserId;
    private BigDecimal dailyCost;
}
