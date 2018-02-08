package com.b2w.desafio.validation;

import org.springframework.http.HttpStatus;

/**
 * Created by matto on 06/02/2018.
 */
//Comentei o Lombok abaixo e coloquei manualmente o Getter e Setter para facilitar o teste pela B2W - Bit
//@Data
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

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
