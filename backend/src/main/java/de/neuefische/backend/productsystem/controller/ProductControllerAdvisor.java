package de.neuefische.backend.productsystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.InputMismatchException;

@ControllerAdvice
public class ProductControllerAdvisor {

    @ExceptionHandler(InputMismatchException.class)
    private ResponseEntity<Object> handleInputMismatchException(InputMismatchException inputMismatchException){
        return new ResponseEntity<>(inputMismatchException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
