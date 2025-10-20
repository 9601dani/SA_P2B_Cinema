package com.codenbugs.cinema.showtime.application.usecase.updateactive;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
public class UpdateActiveCase {
    boolean active;
    UUID id;
}
