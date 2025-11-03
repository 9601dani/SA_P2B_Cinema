package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.PromotionDomainEntity;

import java.util.UUID;

public interface FindingPromotionByIdOutputPort {
    PromotionDomainEntity findingPromotionById(UUID id);
}
