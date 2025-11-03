package com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.mapper;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.inputadapter.dto.TicketResponseDto;
import org.springframework.stereotype.Component;

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
}
