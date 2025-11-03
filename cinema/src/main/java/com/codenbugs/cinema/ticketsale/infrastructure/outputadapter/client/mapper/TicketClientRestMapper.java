package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.mapper;

import com.codenbugs.cinema.ticketsale.domain.model.PromotionDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.PromotionResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TicketClientRestMapper {

    public PromotionDomainEntity toDomain(PromotionResponseDto promotionResponseDto){
        if (promotionResponseDto == null) {
            return null;
        }

        return new PromotionDomainEntity(
                promotionResponseDto.id(),
                BigDecimal.valueOf(promotionResponseDto.discountPercentage()),
                promotionResponseDto.isActive(),
                LocalDate.parse(promotionResponseDto.startDate()),
                 LocalDate.parse(promotionResponseDto.endDate())
        );

    }

}
