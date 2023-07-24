package com.crud.crudfrontendbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {
    private ResponseEntity<Object> handleException(Exception exception, HttpStatus httpStatus) {
        ApiException apiException = new ApiException(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        return handleException(e, HttpStatus.BAD_REQUEST);
    }

}
