package com.samuel.movie_watchlist_api.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidInputAdvice {

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String employeeNotFoundHandler(InvalidInputException ex) {
        return ex.getMessage();
    }

}