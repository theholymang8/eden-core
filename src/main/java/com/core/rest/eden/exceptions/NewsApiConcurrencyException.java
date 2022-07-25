package com.core.rest.eden.exceptions;

public class NewsApiConcurrencyException extends RuntimeException {

    public NewsApiConcurrencyException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }

}
