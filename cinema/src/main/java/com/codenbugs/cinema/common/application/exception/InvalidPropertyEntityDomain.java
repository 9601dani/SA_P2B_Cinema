package com.codenbugs.cinema.common.application.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class InvalidPropertyEntityDomain extends RuntimeException {
    public InvalidPropertyEntityDomain(String message) {
        super(message);
    }
}
