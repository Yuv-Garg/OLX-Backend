package net.olxApplication.controller;

import net.olxApplication.Exception.BadRequest;
import net.olxApplication.Exception.NotExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotExist.class)
    public ResponseEntity<?> handleNotExistException(NotExist exception) {

        return new ResponseEntity<>(new NotExist.NotFoundResponse(exception.getMessage(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<?> handleBadRequestException(BadRequest exception) {

        return new ResponseEntity<>(new BadRequest.BadRequestResponse(exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

}
