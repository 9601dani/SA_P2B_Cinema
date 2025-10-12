package com.codenbugs.cinema.cinema.application.usecase.findcinema;

import com.codenbugs.cinema.cinema.application.ports.input.FindingCinemaByIdAdminInputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByIdAdminOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class FindingCinemaByIdAdminUseCase implements FindingCinemaByIdAdminInputPort {

    private final FindingCinemaByIdAdminOutputPort findingCinemaByIdAdminOutputPort;

    @Override
    public CinemaDomainEntity findCinemaByIdAdmin(UUID id) {
        return findingCinemaByIdAdminOutputPort.findCinemaByIdAdmin(id)
                .orElseThrow(()-> new EntityNotFount("Administrador sin Cine registrado"));
    }
}
