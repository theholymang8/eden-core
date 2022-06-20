package com.core.rest.eden.exceptions;

public class UserHasAlreadySentRequestException extends RuntimeException{

    public UserHasAlreadySentRequestException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

}
