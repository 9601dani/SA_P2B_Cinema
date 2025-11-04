package com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.rest;

import com.codenbugs.cinema.ticketsale.infrastructure.outputadapter.client.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "identity", url = "${client.services.auth-identity}/api/auth-identity")
public interface IdentityRestClient {

    @GetMapping("/v1/users/customers")
    List<UserResponseDto> listAllCustomers();
}