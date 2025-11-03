package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.mapper;

import com.codenbugs.cinema.ticketsale.domain.model.TicketSaleDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.persistence.entity.TicketSaleDBEntity;
import org.springframework.stereotype.Component;

@Component
public class TicketPersistenceMapper {

    public TicketSaleDBEntity toDbEntity(TicketSaleDomainEntity domainEntity) {

        if (domainEntity == null) {
            return  null;
        }

        return TicketSaleDBEntity.builder()
                .showtimeId(domainEntity.getShowtimeId())
                .seatId(domainEntity.getSeatId())
                .userId(domainEntity.getUserId())
                .purchaseDate(domainEntity.getPurchaseDate())
                .price(domainEntity.getPrice())
                .discountPercentage(domainEntity.getDiscountPercentage())
                .priceTotal(domainEntity.getPriceTotal())
                .state(domainEntity.getState())
                .promotionId(domainEntity.getPromotionId())
                .build();

    }

    public TicketSaleDomainEntity toDomainEntity(TicketSaleDBEntity dbEntity) {

        if (dbEntity == null) {
            return  null;
        }

        return new TicketSaleDomainEntity(
                dbEntity.getId(),
                dbEntity.getShowtimeId(),
                dbEntity.getSeatId(),
                dbEntity.getUserId(),
                dbEntity.getPurchaseDate(),
                dbEntity.getPrice(),
                dbEntity.getDiscountPercentage(),
                dbEntity.getPriceTotal(),
                dbEntity.getState(),
                dbEntity.getPromotionId()
        );
    }
}
