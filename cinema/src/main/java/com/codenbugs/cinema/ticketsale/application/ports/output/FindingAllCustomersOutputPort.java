package com.codenbugs.cinema.ticketsale.application.ports.output;

import com.codenbugs.cinema.ticketsale.domain.model.CustomerDomainEntity;

import java.util.List;

public interface FindingAllCustomersOutputPort {
    List<CustomerDomainEntity> findingAllCustomers();
}
