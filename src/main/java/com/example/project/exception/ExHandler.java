package com.example.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InsuranceException.class})
    public ResponseEntity<ErrorMessage> handleInsuranceException(InsuranceException insuranceException) {
        log.error(insuranceException.getMessage());
        ErrorType errorType = insuranceException.getErrorType();
        return new ResponseEntity<>(new ErrorMessage(-10, errorType.name(), insuranceException.getMessage()),
                errorType.getStatus());
    }

    @ExceptionHandler(value = {WrongAgeException.class})
    public ResponseEntity<ErrorMessage> handleWrongAgeException(WrongAgeException e) {
        log.error(e.getMessage());
        ErrorType errorType = e.getErrorType();
        return new ResponseEntity<>(new ErrorMessage(-11, errorType.name(), e.getMessage()),
                errorType.getStatus());
    }

    @ExceptionHandler(value = {ObjectAlreadyExistsException.class})
    public ResponseEntity<ErrorMessage> handleObjectAlreadyExistsException(ObjectAlreadyExistsException e) {
        log.error(e.getMessage());
        ErrorType errorType = e.getErrorType();
        return new ResponseEntity<>(new ErrorMessage(-12, errorType.name(), e.getMessage()),
                errorType.getStatus());
    }

}
