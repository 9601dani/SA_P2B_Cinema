package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.mapper;

import com.codenbugs.cinema.ticketsale.domain.model.CustomerDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.PromotionDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.PromotionResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.UserResponseDto;
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

    public CustomerDomainEntity toDomainCustomer(UserResponseDto userResponseDto){
        if (userResponseDto == null) {
            return null;
        }

        return new CustomerDomainEntity(userResponseDto.id(), userResponseDto.fullName());
    }

}
