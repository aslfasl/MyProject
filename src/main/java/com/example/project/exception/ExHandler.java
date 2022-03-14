package com.example.project.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    public ResponseEntity<ErrorMessage> handleRuntimeException(CustomException exception) {
        log.error(exception.getMessage());
        ErrorType errorType = exception.getErrorType();
        return new ResponseEntity<>(new ErrorMessage(-10, errorType.name(), exception.getMessage()),
                errorType.getStatus());
    }

}
