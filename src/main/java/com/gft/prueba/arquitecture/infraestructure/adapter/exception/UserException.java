package com.gft.prueba.arquitecture.infraestructure.adapter.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus errorCode;
    private String errorMassage;

    public UserException(HttpStatus errorCode, String errorMassage) {
        this.errorCode = errorCode;
        this.errorMassage = errorMassage;
    }

    public UserException() {
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMassage() {
        return errorMassage;
    }

    public void setErrorMassage(String errorMassage) {
        this.errorMassage = errorMassage;
    }
}
