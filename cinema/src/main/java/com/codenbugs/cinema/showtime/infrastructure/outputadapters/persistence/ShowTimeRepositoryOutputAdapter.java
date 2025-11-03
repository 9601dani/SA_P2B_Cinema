package com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.showtime.application.ports.output.*;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.ShowTimeDbEntity;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.entity.mapper.ShowTimeRepositoryMapper;
import com.codenbugs.cinema.showtime.infrastructure.outputadapters.persistence.repository.ShowTimeDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class ShowTimeRepositoryOutputAdapter implements StoringShowTimeOutputPort, FindingShowTimeRangeDateByRoomIdOutputPort,
        ListAllShowTimesByListRoomsIdOutputPort, UpdatingShowTimeActiveByIdOutputPort, ListAllShowTimesByListRoomsIdRangeDateOutputPort ,
        FindingShowTimeByIdOutputPort{

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

    @Override
    @Transactional
    public void updateActive(Boolean active, UUID id) {
        ShowTimeDbEntity showTimeDb = jpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFount("Funcion no encontrada para activar/desactivar"));

        showTimeDb.setActive(active);

        jpaRepository.save(showTimeDb);
    }

    @Override
    public List<ShowTimeDomainEntity> findAllShowTimesByListRoomsIdRangeDate(
            List<UUID> roomsId,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return jpaRepository.findAllByRoomIdInAndRangeDate(roomsId, startTime, endTime)
                .stream()
                .map(mapper::toDomainEntity)
                .toList();
    }

    @Override
    public Optional<ShowTimeDomainEntity> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomainEntity);
    }
}
