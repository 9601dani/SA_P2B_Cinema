package com.codenbugs.cinema.cinema.application.usecase.findcinema;

import com.codenbugs.cinema.cinema.application.ports.input.FindingCinemaByIdInputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindingCinemaByIdUseCase implements FindingCinemaByIdInputPort {

    private final FindingCinemaByIdOutputPort findingCinemaByIdOutputPort;

    @Override
    public CinemaDomainEntity findCinemaById(UUID id) {
        return findingCinemaByIdOutputPort.findCinemaById(id)
                .orElseThrow(() -> new EntityNotFount("Cine No encontrado con id: "+ id));
    }
}
