package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.rest;

import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.PromotionResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "promotion", url = "${client.services.promotion}/api/promotion")
public interface PromotionRestClient {

    @GetMapping("/v1/promotions/{id}")
    PromotionResponseDto findPromotionById(@PathVariable UUID id);
}
