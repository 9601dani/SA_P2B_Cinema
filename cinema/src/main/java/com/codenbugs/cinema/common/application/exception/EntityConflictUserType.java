package com.codenbugs.cinema.common.application.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT)
public class EntityConflictUserType extends RuntimeException {
    public EntityConflictUserType(String message) {
        super(message);
    }
}
