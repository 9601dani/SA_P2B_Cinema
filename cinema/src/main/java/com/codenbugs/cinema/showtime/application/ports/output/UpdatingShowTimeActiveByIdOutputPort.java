package com.codenbugs.cinema.showtime.application.ports.output;

import java.util.UUID;

public interface UpdatingShowTimeActiveByIdOutputPort {
    void updateActive(Boolean active, UUID id);
}
