package com.example.dhproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientErrorException extends RuntimeException{


    private final HttpStatus status;

    public ClientErrorException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
