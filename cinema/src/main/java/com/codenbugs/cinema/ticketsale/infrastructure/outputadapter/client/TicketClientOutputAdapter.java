package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client;

import com.codenbugs.cinema.common.infrastructure.exception.ExternalServiceException;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingPromotionByIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.PromotionDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.PromotionResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.mapper.TicketClientRestMapper;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.rest.PromotionRestClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketClientOutputAdapter implements FindingPromotionByIdOutputPort {

    private final TicketClientRestMapper mapper;
    private final PromotionRestClient promotionRestClient;

    @Override
    public PromotionDomainEntity findingPromotionById(UUID id) {
        try {
            PromotionResponseDto promotionResponseDto = promotionRestClient.findPromotionById(id);
            return mapper.toDomain(promotionResponseDto);
        }catch (FeignException e) {
            throw new ExternalServiceException("No se pudo obtener la promocion para la compra del ticket.");
        }
    }
}
