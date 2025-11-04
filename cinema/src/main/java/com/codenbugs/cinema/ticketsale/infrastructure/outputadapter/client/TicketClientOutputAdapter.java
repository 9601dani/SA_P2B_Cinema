package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client;

import com.codenbugs.cinema.common.infrastructure.exception.ExternalServiceException;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingAllCustomersOutputPort;
import com.codenbugs.cinema.ticketsale.application.ports.output.FindingPromotionByIdOutputPort;
import com.codenbugs.cinema.ticketsale.domain.model.CustomerDomainEntity;
import com.codenbugs.cinema.ticketsale.domain.model.PromotionDomainEntity;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.PromotionResponseDto;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.mapper.TicketClientRestMapper;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.rest.IdentityRestClient;
import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.rest.PromotionRestClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketClientOutputAdapter implements FindingPromotionByIdOutputPort, FindingAllCustomersOutputPort {

    private final TicketClientRestMapper mapper;
    private final PromotionRestClient promotionRestClient;
    private final IdentityRestClient identityRestClient;

    @Override
    public PromotionDomainEntity findingPromotionById(UUID id) {
        try {
            PromotionResponseDto promotionResponseDto = promotionRestClient.findPromotionById(id);
            return mapper.toDomain(promotionResponseDto);
        }catch (FeignException e) {
            throw new ExternalServiceException("No se pudo obtener la promocion para la compra del ticket.");
        }
    }

    @Override
    public List<CustomerDomainEntity> findingAllCustomers() {
        try {
            return identityRestClient.listAllCustomers()
                    .stream()
                    .map(mapper::toDomainCustomer)
                    .toList();
        }catch (FeignException e) {
            throw new ExternalServiceException("No se obtener los clientes para el reporte.");
        }
    }
}
