package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {UserDoesNotExistException.class})
    public ResponseEntity<Object> handleDoesNotExistException(UserDoesNotExistException e) {
        log.error(e.getMessage());
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiExceptionTemplate apiException = new ApiExceptionTemplate(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("GMT+3")));
        return new ResponseEntity<>(apiException, notFound);
    }

}
