package com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence;

import com.codenbugs.cinema.cinema.application.ports.output.*;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.CinemaDbEntity;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.entity.mapper.CinemaRepositoryMapper;
import com.codenbugs.cinema.cinema.infrastructure.outputadapters.persistence.repository.CinemaDbEntityJpaRepository;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.common.infrastructure.annotation.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class CinemaRepositoryOutputAdapter implements StoringCinemaOutputPort, FindingCinemaByNameOutputPort,
        FindingCinemaByIdAdminOutputPort, UpdatingCinemaByIdOutputPort, FindingCinemaByIdOutputPort {

    private final CinemaDbEntityJpaRepository cinemaDbEntityJpaRepository;
    private final CinemaRepositoryMapper mapper;

    @Override
    @Transactional
    public CinemaDomainEntity save(CinemaDomainEntity cinema) {
        CinemaDbEntity cinemaDb = mapper.toDbEntity(cinema);
        cinemaDbEntityJpaRepository.save(cinemaDb);
        return mapper.toDomainEntity(cinemaDb);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CinemaDomainEntity> findCinemaByName(String name) {
        return cinemaDbEntityJpaRepository.findByName(name)
                .map(mapper::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CinemaDomainEntity> findCinemaByIdAdmin(UUID id) {
        return cinemaDbEntityJpaRepository.findByAdminUserId(id)
                .map((mapper::toDomainEntity));
    }

    @Override
    @Transactional
    public CinemaDomainEntity update(UUID cinemaId, CinemaDomainEntity cinema) {
        CinemaDbEntity cinemaDb = cinemaDbEntityJpaRepository.findById(cinemaId)
                .orElseThrow(() -> new EntityNotFount("Cinema no encontrado con el id: "+cinemaId));

        cinemaDb.setName(cinema.getName());
        cinemaDb.setAddress(cinema.getAddress());
        cinemaDb.setImageUrl(cinema.getImageUrl());
        cinemaDb.setDailyCost(cinema.getDailyCost());

        cinemaDbEntityJpaRepository.save(cinemaDb);
        return mapper.toDomainEntity(cinemaDb);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CinemaDomainEntity> findCinemaById(UUID id) {
        return cinemaDbEntityJpaRepository.findById(id)
                .map((mapper::toDomainEntity));
    }
}
