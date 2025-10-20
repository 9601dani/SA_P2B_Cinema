package com.codenbugs.cinema.showtime.application.ports.input;

import com.codenbugs.cinema.showtime.application.usecase.createshowtime.CreateShowTimeCaseDto;
import jakarta.validation.Valid;

public interface CreatingShowTimeInputPort {
    void creatShowTime(@Valid CreateShowTimeCaseDto createShowTimeCaseDto);
}
