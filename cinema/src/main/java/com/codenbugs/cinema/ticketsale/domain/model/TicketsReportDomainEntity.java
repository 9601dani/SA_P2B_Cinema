package com.codenbugs.cinema.ticketsale.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@DomainEntity
@Getter
@Setter
public class TicketsReportDomainEntity {
    private UUID ticketId;
    private String nameSeat;
    private String userName;
    // datos del swhotime
    private UUID showtimeId;
    private String movieTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instant purchaseDate;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal priceTotal;

    public TicketsReportDomainEntity(UUID ticketId, String nameSeat, String userName, UUID showtimeId, String movieTitle, LocalDateTime startTime, LocalDateTime endTime, Instant purchaseDate, BigDecimal price, BigDecimal discountPercentage, BigDecimal priceTotal) {
        this.ticketId = ticketId;
        this.nameSeat = nameSeat;
        this.userName = userName;
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.priceTotal = priceTotal;
    }
}
