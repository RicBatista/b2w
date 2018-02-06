package com.b2w.desafio.validation;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Created by matto on 06/02/2018.
 */
@Data
public class SwapiValidationException extends Exception {

    private HttpStatus httpStatus;

    public SwapiValidationException() {
        super();
    }

    public SwapiValidationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public SwapiValidationException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
