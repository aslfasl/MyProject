package com.example.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    NOT_FOUND(-100, HttpStatus.BAD_REQUEST),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_EXISTS(-200, HttpStatus.CONFLICT);

    private final HttpStatus status;
    private int code;

    ErrorType(HttpStatus status) {
        this.status = status;
    }

    ErrorType(int code, HttpStatus status) {
        this.status = status;
        this.code = code;
    }
}
