package com.codenbugs.cinema.cinema.application.ports.output;

import java.util.UUID;

public interface CinemaCreateWalletEventPort {
    void publishCinemaCreateWallet(UUID cinemaId);
}
