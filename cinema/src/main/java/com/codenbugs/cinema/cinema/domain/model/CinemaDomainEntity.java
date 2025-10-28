package com.codenbugs.cinema.cinema.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
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
    private Instant createdAt;

    public CinemaDomainEntity(String name, String imageUrl, String address, UUID adminUserId, BigDecimal dailyCost) {
        this.name = name.toUpperCase();
        this.imageUrl = imageUrl;
        this.address = address;
        this.adminUserId = adminUserId;
        this.dailyCost = dailyCost;
        this.validate();
    }

    public CinemaDomainEntity(UUID id, String name, String imageUrl, String address, UUID adminUserId, BigDecimal dailyCost, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.adminUserId = adminUserId;
        this.dailyCost = dailyCost;
        this.createdAt = createdAt;
    }

    private void validate(){
        // validar nombre no puede ser menor a 3 letras
        if (this.name == null || this.name.length()< 3){
            throw new InvalidPropertyEntityDomain("Nombre del cine no valido, debe ser mayor a 3 letras");
        }

        if (this.dailyCost == null || this.dailyCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPropertyEntityDomain("Costo por dia del Cine, debe ser mayor a cero");
        }
    }
}
