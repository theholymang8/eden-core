package com.core.rest.eden.controllers.exceptionHandlers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Unable to upload. File is too large!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<String>> handleIoException(IOException ioException) {
        log.info("Exception during File upload: {}", ioException);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Problem occurred during the upload of the file!").build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullCookies(NullPointerException nullPointerException) {
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Cookies are null or expired").build(), HttpStatus.BAD_REQUEST);
    }
}
