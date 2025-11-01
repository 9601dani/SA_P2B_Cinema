package com.codenbugs.cinema.cinema.application.usecase.createcinema;

import com.codenbugs.cinema.cinema.application.ports.input.CreatingCinemaInputPort;
import com.codenbugs.cinema.cinema.application.ports.output.CinemaCreateWalletEventPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingCinemaByNameOutputPort;
import com.codenbugs.cinema.cinema.application.ports.output.StoringCinemaOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreatingCinemaUseCase implements CreatingCinemaInputPort {

    private final StoringCinemaOutputPort  storingCinemaOutputPort;
    private final FindingCinemaByNameOutputPort findingCinemaByNameOutputPort;
    private final CinemaCreateWalletEventPort cinemaCreateWalletEventPort;

    @Override
    @Transactional
    public CinemaDomainEntity createCinema(CreateCinemaDto createCinemaDto) {

        CinemaDomainEntity domain = createCinemaDto.toDomain();

        // validaciones
        if (findingCinemaByNameOutputPort.findCinemaByName(domain.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("Ya existe un cine con ese nombre");
        }

        var cinemaCreated = storingCinemaOutputPort.save(domain);

        cinemaCreateWalletEventPort.publishCinemaCreateWallet(cinemaCreated.getId());

        return cinemaCreated;
    }
}
