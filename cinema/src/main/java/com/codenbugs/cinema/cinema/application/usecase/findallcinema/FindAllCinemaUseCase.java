package com.codenbugs.cinema.cinema.application.usecase.findallcinema;

import com.codenbugs.cinema.cinema.application.ports.input.FindAllCinemaInputPort;
import com.codenbugs.cinema.cinema.application.ports.output.FindingAllCinemaOutputPort;
import com.codenbugs.cinema.cinema.domain.model.CinemaDomainEntity;
import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UseCase
public class FindAllCinemaUseCase implements FindAllCinemaInputPort {
    private final FindingAllCinemaOutputPort findingAllCinemaOutputPort;

    @Autowired
    public FindAllCinemaUseCase(FindingAllCinemaOutputPort findingAllCinemaOutputPort) {
        this.findingAllCinemaOutputPort = findingAllCinemaOutputPort;
    }

    @Override
    public List<CinemaDomainEntity> findAll() {
        return this.findingAllCinemaOutputPort.findAllCinemas();
    }
}
