package com.core.rest.eden.exceptions;

public class AlreadyFriendsException extends RuntimeException {

    public AlreadyFriendsException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

}
