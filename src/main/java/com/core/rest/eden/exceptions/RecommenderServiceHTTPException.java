package com.core.rest.eden.exceptions;

public class RecommenderServiceHTTPException extends RuntimeException {

    public RecommenderServiceHTTPException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }
}
