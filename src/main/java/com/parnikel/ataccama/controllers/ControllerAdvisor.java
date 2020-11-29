package com.parnikel.ataccama.controllers;

import com.parnikel.ataccama.exceptions.DbConnectionException;
import com.parnikel.ataccama.exceptions.NoDatabaseForId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DbConnectionException.class)
    public ResponseEntity<Object> handleConnectionException(DbConnectionException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Could not connect to database");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoDatabaseForId.class)
    public ResponseEntity<Object> handleDatabaseNotFoundException(NoDatabaseForId ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "No database found");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Unknown error has occured");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
