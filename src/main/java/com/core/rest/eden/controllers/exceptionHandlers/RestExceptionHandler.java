package com.core.rest.eden.controllers.exceptionHandlers;

import com.core.rest.eden.controllers.transfer.ApiError;
import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.exceptions.UserAlreadyExistsException;
import com.core.rest.eden.exceptions.UserHasAlreadySentRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.info("File Size too large", exc);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Unable to upload. File is too large!").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHasAlreadySentRequestException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadySentRequestException(UserHasAlreadySentRequestException exc) {
        log.info("Has already sent a request", exc);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("A request to this user has been sent already").build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<String>> handleIoException(IOException ioException) {
        log.info("Exception during File upload: {}", ioException);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Problem occurred during the upload of the file!").build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullCookies(NullPointerException nullPointerException) {
        log.info("Null Cookies sent on request: {}", nullPointerException);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("Cookies are null or expired").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponse<String>> handleNoSuchElement(NoSuchElementException noSuchElementException) {
        log.info("No such element exists on the database: {}", noSuchElementException);
        return new ResponseEntity<>(ApiResponse.<String>builder().data("No such element exists").build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleDoubleRegisteredUser(UserAlreadyExistsException userAlreadyExistsException) {
        log.info("Double User registration occurred: {}", userAlreadyExistsException);
        return new ResponseEntity<>(ApiResponse.<String>builder()
                //.data("A user with this username/email already exists")
                .apiError(ApiError.builder()
                        .message("A user with this username/email already exists")
                        .build())
                .build(), HttpStatus.CONFLICT);
    }
}
