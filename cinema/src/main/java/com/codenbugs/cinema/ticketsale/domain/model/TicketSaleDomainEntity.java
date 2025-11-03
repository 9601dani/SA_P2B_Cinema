package com.codenbugs.cinema.ticketsale.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import com.codenbugs.cinema.common.application.exception.InvalidPropertyEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.enums.StateTicket;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@DomainEntity
@Getter
public class TicketSaleDomainEntity {
    private UUID id;
    private UUID showtimeId;
    private UUID seatId;
    private UUID userId;
    private Instant purchaseDate;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal priceTotal;
    private StateTicket state;
    private UUID promotionId;
    @Setter
    private UUID walletId;


    // cuando viene de db
    public TicketSaleDomainEntity(UUID id, UUID showtimeId, UUID seatId, UUID userId, Instant purchaseDate, BigDecimal price, BigDecimal discountPercentage, BigDecimal priceTotal, StateTicket state, UUID promotionId) {
        this.id = id;
        this.showtimeId = showtimeId;
        this.seatId = seatId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.priceTotal = priceTotal;
        this.state = state;
        this.promotionId = promotionId;
    }

    // para crear uno nuevo
    public TicketSaleDomainEntity(UUID showtimeId, UUID seatId, UUID userId, UUID walletId, UUID promotionId) {
        this.showtimeId = showtimeId;
        this.seatId = seatId;
        this.userId = userId;
        this.state = StateTicket.PENDING_PAYMENT;
        this.promotionId = promotionId;
        this.walletId = walletId;
        this.validate();
    }


    // nuevo pero con los datos obtendidos de la promocion y el precio del showtime
    public TicketSaleDomainEntity(UUID showtimeId, UUID seatId, UUID userId, BigDecimal price, BigDecimal discountPercentage, StateTicket state, UUID walletId, UUID promotionId) {
        this.showtimeId = showtimeId;
        this.seatId = seatId;
        this.userId = userId;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.state = state;
        this.promotionId = promotionId;
        this.walletId = walletId;
        this.purchaseDate = Instant.now();
        this.calculateTotalPrice();
    }

    private void validate(){
        // validar reglas de negocio
        if (showtimeId == null){
            throw new InvalidPropertyEntityDomain("funcion no puede ser nulo");
        }

        if (seatId == null){
            throw new InvalidPropertyEntityDomain("asiento no puede ser nulo");
        }

        if (userId == null){
            throw new InvalidPropertyEntityDomain("usuario no puede ser nulo");
        }

        if (walletId == null){
            throw new InvalidPropertyEntityDomain("cartera dgital no puede ser nulo");
        }
    }

    // funcion para calcular el precio total
    public void calculateTotalPrice() {
        if (this.price == null || this.discountPercentage == null) {
            throw new InvalidPropertyEntityDomain("precio o descuento no pueden ser nulos para calcular el precio total");
        }

        // precio no puede ser negativo
        if (this.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPropertyEntityDomain("el precio no puede ser menor a cero");
        }

        // descuento no puede ser negativo
        if (this.discountPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPropertyEntityDomain("el descuento no puede ser menor a 0%");
        }

        // descuento no puede ser mayor a 100%
        if (this.discountPercentage.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidPropertyEntityDomain("el porcentaje de descuento no puede ser mayor al 100%");
        }

        // si no hay promoción = descuento debe ser 0
        if (this.promotionId == null && this.discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            throw new InvalidPropertyEntityDomain("no puedes aplicar descuento sin promocion");
        }

        BigDecimal discountAmount =
                this.price.multiply(this.discountPercentage).divide(new BigDecimal("100"));

        this.priceTotal = this.price.subtract(discountAmount);
    }

}
