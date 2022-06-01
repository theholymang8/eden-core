package com.core.rest.eden.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }
}
