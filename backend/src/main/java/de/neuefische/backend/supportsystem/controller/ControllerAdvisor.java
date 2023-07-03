package de.neuefische.backend.supportsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInputMismatchException(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity<>(illegalArgumentException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleInputMismatchException(NoSuchElementException noSuchElementException) {
        return new ResponseEntity<>(noSuchElementException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleInputMismatchException(UsernameNotFoundException usernameNotFoundException) {
        return new ResponseEntity<>(usernameNotFoundException.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<Object> handleInputMismatchException(IllegalAccessException illegalAccessException) {
        return new ResponseEntity<>(illegalAccessException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleInputMismatchException(NullPointerException nullPointerException) {
        return new ResponseEntity<>(nullPointerException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
