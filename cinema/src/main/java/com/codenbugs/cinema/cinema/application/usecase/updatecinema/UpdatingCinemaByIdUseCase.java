package com.codenbugs.cinema.cinema.application.usecase.updatecinema;

import com.codenbugs.cinema.cinema.application.ports.input.UpdatingCinemaByIdInputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByNameOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.UpdatingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdatingCinemaByIdUseCase implements UpdatingCinemaByIdInputPort {

    private final UpdatingCinemaByIdOutputPort updatingCinemaByIdOutputPort;
    private final FindingCinemaByNameOutputPort findingCinemaByNameOutputPort;
    private final FindingCinemaByIdOutputPort findingCinemaByIdOutputPort;


    @Override
    @Transactional
    public CinemaDomainEntity updatingCinemaById(UUID cinemaId, UpdateCinemaDto updateCinemaDto) {

        CinemaDomainEntity domain = updateCinemaDto.toDomain();

        // validaciones
        CinemaDomainEntity currentCinema = findingCinemaByIdOutputPort.findCinemaById(cinemaId)
                .orElseThrow(() -> new EntityNotFount("Cinema no encontado para actualizar, Id: " + cinemaId));

        if (!currentCinema.getName().equalsIgnoreCase(updateCinemaDto.getName()) &&
                findingCinemaByNameOutputPort.findCinemaByName(domain.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("Ya existe un cine con ese nombre");
        }

        return updatingCinemaByIdOutputPort.update(cinemaId, domain);
    }
}
