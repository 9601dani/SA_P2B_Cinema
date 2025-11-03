package com.codenbugs.cinema.ticketsale.domain.model;

import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class PromotionDomainEntity {
    private UUID id;
    private BigDecimal discountPercentage;
    private boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;

    public PromotionDomainEntity(UUID id, BigDecimal discountPercentage, boolean isActive, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.discountPercentage = discountPercentage;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.validate();
    }

    // validate
    public void validate() {
        LocalDate today = LocalDate.now();
        if (!isActive) {
            throw new InvalidPropertyEntityDomain("La promoción no está activa, para realizar el descuento");
        }

        if (today.isBefore(startDate) || today.isAfter(endDate)) {
            throw new InvalidPropertyEntityDomain("La promoción no es válida en la fecha actual");
        }

        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPropertyEntityDomain("El descuento debe ser mayor a cero");
        }

        if (discountPercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidPropertyEntityDomain("El descuento no puede ser mayor a 100%");
        }
    }

}