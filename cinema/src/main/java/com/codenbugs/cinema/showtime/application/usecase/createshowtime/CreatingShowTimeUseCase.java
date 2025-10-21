package com.codenbugs.cinema.showtime.application.usecase.createshowtime;

import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.common.application.exception.EntityAlreadyExistsException;
import com.codenbugs.cinema.common.application.exception.EntityNotFount;
import com.codenbugs.cinema.room.application.ports.output.FindingRoomByIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.input.CreatingShowTimeInputPort;
import com.codenbugs.cinema.showtime.application.ports.output.FindMovieByIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.FindingShowTimeRangeDateByRoomIdOutputPort;
import com.codenbugs.cinema.showtime.application.ports.output.NotificationCreatedShowTimeEventPort;
import com.codenbugs.cinema.showtime.application.ports.output.StoringShowTimeOutputPort;
import com.codenbugs.cinema.showtime.domain.model.MovieDomainEntity;
import com.codenbugs.cinema.showtime.domain.model.ShowTimeDomainEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@UseCase
@Validated
@RequiredArgsConstructor
public class CreatingShowTimeUseCase implements CreatingShowTimeInputPort {

    private final StoringShowTimeOutputPort storingShowTimeOutputPort;
    private final FindMovieByIdOutputPort  findMovieByIdOutputPort;
    private final FindingShowTimeRangeDateByRoomIdOutputPort findingShowTimeRangeDateByRoomIdOutputPort;
    private final FindingRoomByIdOutputPort findingRoomByIdOutputPort;
    private final NotificationCreatedShowTimeEventPort notificationCreatedShowTimeEventPort;

    @Override
    @Transactional
    public void creatShowTime(CreateShowTimeCaseDto createShowTimeCaseDto) {
        ShowTimeDomainEntity domain = createShowTimeCaseDto.toDomain();
        domain.validateCurrentDate();

        // validate room
        if (findingRoomByIdOutputPort.findingRoomById(domain.getRoomId()).isEmpty()) {
            throw new EntityNotFount("No existe la sala para crear la funcion");
        }

        // get durationMinutes movie service and validate active movie
        MovieDomainEntity movie = findMovieByIdOutputPort.findById(domain.getMovieId());
        movie.validateActive();

        // calculated endDate domain
        domain.calculateEndTime(movie.getDurationMinutes());

        // validate no traslapes en funciones
        if (findingShowTimeRangeDateByRoomIdOutputPort.existRegistersRangeDate(domain.getRoomId(), domain.getStartTime(), domain.getEndTime())) {
            throw new EntityAlreadyExistsException("La sala ya tiene una funcion en esa fecha y hora");
        }

        // save
        storingShowTimeOutputPort.save(domain);

        // notification event
        notificationCreatedShowTimeEventPort.publisherNewShowtimeCustomers(domain);
    }
}
