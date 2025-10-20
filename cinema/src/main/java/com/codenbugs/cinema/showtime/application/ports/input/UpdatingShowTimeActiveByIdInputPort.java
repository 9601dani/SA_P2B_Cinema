package com.codenbugs.cinema.showtime.application.ports.input;

import com.codenbugs.cinema.showtime.application.usecase.updateactive.UpdateActiveCase;
import jakarta.validation.Valid;

public interface UpdatingShowTimeActiveByIdInputPort {
    void  updateActive(@Valid UpdateActiveCase updateActiveCase);
}
