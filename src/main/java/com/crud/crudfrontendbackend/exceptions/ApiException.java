package com.crud.crudfrontendbackend.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime zonedDateTime;

    public ApiException(String message,
                        HttpStatus httpStatus,
                        ZonedDateTime zonedDateTime) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.zonedDateTime = zonedDateTime;
    }
}

