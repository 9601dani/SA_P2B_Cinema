package com.codenbugs.cinema.ticketsale.domain.model;

import com.codenbugs.cinema.common.application.domain.annotations.DomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@DomainEntity
@Getter
@Setter
public class ReportTicketsPerRoomEntityDomain {
    // datos del room
    private UUID roomId;
    private Integer capacity;
    private String imageUrl;
    private String name;
    private Integer rows;
    private Integer columns;
    // datos de los tickets vendidos
    List<TicketsReportDomainEntity> ticketsSold;

    public ReportTicketsPerRoomEntityDomain(UUID roomId, Integer capacity, String imageUrl, String name, Integer rows, Integer columns) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.imageUrl = imageUrl;
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }
}
