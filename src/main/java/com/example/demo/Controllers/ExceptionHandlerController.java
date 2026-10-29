package com.example.demo.Controllers;

import com.example.demo.DTOs.ErrorResponse;
import com.example.demo.Exceptions.EmailException;
import com.example.demo.Exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(UserException.class)
    ResponseEntity<ErrorResponse> handler(UserException ex) {

        logger.warn("UserException occurred: {}", ex.getMessage(), ex);

        ErrorResponse err = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException ex) {

        logger.error("Database error occurred", ex);

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse("Database unavailable"));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorResponse> emailException(EmailException e){
        logger.error(e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> badRequest(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage() == null ? "Invalid request" : ex.getMessage()));
    }

}
