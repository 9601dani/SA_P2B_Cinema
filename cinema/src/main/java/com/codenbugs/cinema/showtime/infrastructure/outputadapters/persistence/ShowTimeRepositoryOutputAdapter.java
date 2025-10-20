package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.showtime.application.ports.output.FindingShowTimeRangeDateByRoomIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.ListAllShowTimesByListRoomsIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.StoringShowTimeOutputPort;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.mapper.ShowTimeRepositoryMapper;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.repository.ShowTimeDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class ShowTimeRepositoryOutputAdapter implements StoringShowTimeOutputPort, FindingShowTimeRangeDateByRoomIdOutputPort,
        ListAllShowTimesByListRoomsIdOutputPort {

    private final ShowTimeRepositoryMapper mapper;
    private final ShowTimeDbEntityJpaRepository jpaRepository;

    @Override
    @Transactional
    public void save(ShowTimeDomainEntity showTimeDomainEntity) {
        ShowTimeDbEntity dbEntity = mapper.timeDbEntity(showTimeDomainEntity);
        jpaRepository.save(dbEntity);
    }

    @Override
    public boolean existRegistersRangeDate(UUID roomId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.existsByDateRangeAndRoomId(startDate, endDate, roomId);
    }


    @Override
    public List<ShowTimeDomainEntity> findAllShowTimesByListRoomsId(List<UUID> roomsId) {
        return jpaRepository.findAllByRoomIdIn(roomsId)
                .stream()
                .map(mapper::toDomainEntity)
                .toList();
    }
}
