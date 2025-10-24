package com.codenbugs.cinema.room.infrastructure.outputadapters.persistence;


import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import com.codenbugs.cinema.room.application.ports.output.*;
import com.codenbugs.cinema.room.domain.RoomDomainEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.RoomDbEntity;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.entity.mapper.RoomRepositoryMapper;
import com.codenbugs.cinema.room.infrastructure.outputadapters.persistence.repository.RoomDbEntityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class RoomRepositoryOutputAdapter implements StoringRoomOutputPort, FindingAllRoomsByCinemaIdOutputPort,
        UpdatingRoomByIdOutputPort, FindingRoomByIdOutputPort, FindingRoomByNameOutputPort {

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

    @Override
    @Transactional
    public RoomDomainEntity updatingRoomById(UUID roomId, RoomDomainEntity roomDomain) {
        RoomDbEntity roomDb = roomDbEntityJpaRepository.findById(roomId)
                .orElseThrow(()-> new EntityNotFount("Sala no encontrada con id: " + roomId));

        roomDb.setName(roomDomain.getName());
        roomDb.setDescription(roomDomain.getDescription());
        roomDb.setImageUrl(roomDomain.getImageUrl());
        roomDb.setBlocked(roomDomain.isBlocked());
        roomDb.setCommentsEnabled(roomDomain.isCommentsEnabled());

        RoomDbEntity roomDBUpdate = roomDbEntityJpaRepository.save(roomDb);

        return mapper.toDomainEntity(roomDBUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDomainEntity> findingRoomById(UUID roomId) {
        return roomDbEntityJpaRepository.findById(roomId)
                .map(mapper::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDomainEntity> findingRoomByNameAndCinemaId(String name, UUID cinemaId) {
        return roomDbEntityJpaRepository.findByNameAndCinemaId(name, cinemaId)
                .map(mapper::toDomainEntity);
    }
}
