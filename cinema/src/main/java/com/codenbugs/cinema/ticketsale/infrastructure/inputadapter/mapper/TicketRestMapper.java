package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.ticketsale.domain.model.ReportTicketsPerRoomEntityDomain;
import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.TicketsReportDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.ReportTicketsPerRoomResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.TicketResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.TicketsReportResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketRestMapper {

    public TicketResponseDto toResponseDto(TicketSaleDomainEntity domainEntity) {

        if (domainEntity == null) {
            return null;
        }

        return TicketResponseDto.builder()
                .id(domainEntity.getId())
                .showtimeId(domainEntity.getShowtimeId())
                .seatId(domainEntity.getSeatId())
                .userId(domainEntity.getUserId())
                .purchaseDate(domainEntity.getPurchaseDate())
                .price(domainEntity.getPrice())
                .discountPercentage(domainEntity.getDiscountPercentage())
                .priceTotal(domainEntity.getPriceTotal())
                .state(domainEntity.getState().name())
                .promotionId(domainEntity.getPromotionId())
                .build();
    }

    public ReportTicketsPerRoomResponseDto toReportResponseDto(ReportTicketsPerRoomEntityDomain domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        return ReportTicketsPerRoomResponseDto.builder()
                .roomId(domainEntity.getRoomId())
                .capacity(domainEntity.getCapacity())
                .imageUrl(domainEntity.getImageUrl())
                .name(domainEntity.getName())
                .rows(domainEntity.getRows())
                .columns(domainEntity.getColumns())
                .ticketsSold(
                        domainEntity.getTicketsSold() == null
                                ? List.of()
                                : domainEntity.getTicketsSold().stream()
                                .map(this::toTicketReportResponseDto)
                                .toList()
                )
                .build();
    }


    public TicketsReportResponseDto toTicketReportResponseDto(TicketsReportDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        return TicketsReportResponseDto.builder()
                .ticketId(domainEntity.getTicketId())
                .showtimeId(domainEntity.getShowtimeId())
                .nameSeat(domainEntity.getNameSeat())
                .userName(domainEntity.getUserName())
                .movieTitle(domainEntity.getMovieTitle())
                .startTime(domainEntity.getStartTime())
                .endTime(domainEntity.getEndTime())
                .purchaseDate(domainEntity.getPurchaseDate())
                .price(domainEntity.getPrice())
                .discountPercentage(domainEntity.getDiscountPercentage())
                .priceTotal(domainEntity.getPriceTotal())
                .build();
    }
}
