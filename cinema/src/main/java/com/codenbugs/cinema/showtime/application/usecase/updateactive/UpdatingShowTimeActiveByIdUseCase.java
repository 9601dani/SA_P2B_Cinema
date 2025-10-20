package com.codenbugs.cinema.showtime.application.usecase.updateactive;


import com.codenbugs.cinema.common.application.annotations.UseCase;
import com.codenbugs.cinema.showtime.application.ports.input.UpdatingShowTimeActiveByIdInputPort;
import com.codenbugs.cinema.showtime.application.ports.output.UpdatingShowTimeActiveByIdOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@UseCase
@Validated
@RequiredArgsConstructor
public class UpdatingShowTimeActiveByIdUseCase implements UpdatingShowTimeActiveByIdInputPort {

    private final UpdatingShowTimeActiveByIdOutputPort outputPort;

    @Override
    public void updateActive(UpdateActiveCase updateActiveCase) {
        outputPort.updateActive(updateActiveCase.isActive(), updateActiveCase.getId());
    }
}
