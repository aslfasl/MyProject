package com.example.project.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{

    private final ErrorType errorType;

    public CustomException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }
}
