package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.seat.application.ports.output.StoringAllSeatsOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.SeatDbEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.mapper.SeatRepositoryMapper;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.repository.SeatDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class SeatRepositoryOutputAdapter implements StoringAllSeatsOutputPort {

    private final SeatDbEntityJpaRepository seatDbEntityJpaRepository;
    private final SeatRepositoryMapper mapper;

    @Override
    @Transactional
    public void saveAll(List<SeatDomainEntity> seats) {
        List<SeatDbEntity> seatsDB = seats.stream()
                .map(mapper::toDbEntity)
                .toList();

        seatDbEntityJpaRepository.saveAll(seatsDB);
    }
}
