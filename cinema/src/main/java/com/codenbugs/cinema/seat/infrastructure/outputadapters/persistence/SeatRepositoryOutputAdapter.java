package com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.seat.application.ports.output.FindingAllSeatsByRoomIdOutputPort;
import com.codenbugs.cinema.seat.application.ports.output.FindingSeatByIdOutputPort;
import com.codenbugs.cinema.seat.application.ports.output.StoringAllSeatsOutputPort;
import com.codenbugs.cinema.seat.domain.SeatDomainEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.SeatDbEntity;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.entity.mapper.SeatRepositoryMapper;
import com.codenbugs.cinema.seat.infrastructure.outputadapters.persistence.repository.SeatDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class SeatRepositoryOutputAdapter implements StoringAllSeatsOutputPort, FindingAllSeatsByRoomIdOutputPort,
        FindingSeatByIdOutputPort {

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

    @Override
    @Transactional(readOnly = true)
    public List<SeatDomainEntity> findAllSeatsByRoomId(UUID roomId) {
        return seatDbEntityJpaRepository.findAllByRoomId(roomId)
                .stream()
                .map(mapper::toSeatDomainEntity)
                .toList();
    }

    @Override
    public Optional<SeatDomainEntity> findById(UUID seatId) {
        return seatDbEntityJpaRepository.findById(seatId)
                .map(mapper::toSeatDomainEntity);
    }
}
