package com.codenbugs.cinema.common.infrastructure.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED)
public class FailedAuthenticateException extends RuntimeException {
    public FailedAuthenticateException(String message) {
        super(message);
    }
}
