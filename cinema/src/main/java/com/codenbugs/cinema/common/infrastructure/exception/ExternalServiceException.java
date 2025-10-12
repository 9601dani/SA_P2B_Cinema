package com.codenbugs.cinema.common.infrastructure.exception;


import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FAILED_DEPENDENCY;

@ResponseStatus(value = FAILED_DEPENDENCY)
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
