package com.codenbugs.cinema.cinema.application.usecase.createcinema;

import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@AllArgsConstructor
public class CreateCinemaDto {
    String name;
    String address;
    UUID adminUserId;
    BigDecimal dailyCost;
    String imageUrl;

    public CinemaDomainEntity toDomain(){
        return new CinemaDomainEntity(name, imageUrl, address, adminUserId, dailyCost);
    }
}
