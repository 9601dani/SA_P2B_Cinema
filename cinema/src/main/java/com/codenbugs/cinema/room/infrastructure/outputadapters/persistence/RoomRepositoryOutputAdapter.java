package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence;


import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.room.application.ports.output.FindingAllRoomsByCinemaIdOutputPort;
import com.codenbugs.cinema.room.application.ports.output.StoringRoomOutputPort;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.RoomDbEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.mapper.RoomRepositoryMapper;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.repository.RoomDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class RoomRepositoryOutputAdapter implements StoringRoomOutputPort, FindingAllRoomsByCinemaIdOutputPort {

    private final RoomRepositoryMapper mapper;
    private final RoomDbEntityJpaRepository roomDbEntityJpaRepository;

    @Override
    @Transactional
    public RoomDomainEntity save(RoomDomainEntity roomDomain) {
        RoomDbEntity roomDbEntity = mapper.toDbEntity(roomDomain);
        RoomDbEntity savedRoomDB =  roomDbEntityJpaRepository.save(roomDbEntity);
        return mapper.toDomainEntity(savedRoomDB);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDomainEntity> findAllByCinemaId(UUID cinemaId) {
        return roomDbEntityJpaRepository.findAllByCinemaId(cinemaId)
                .stream()
                .map(mapper::toDomainEntity)
                .toList();
    }
}
