package com.core.rest.eden.controllers.transfer;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Value
@Builder
@JsonView(Views.Public.class)
public class ApiResponse<T> implements Serializable {
    String transactionId = UUID.randomUUID().toString().toUpperCase();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss.SSS")
    Date createdAt = new Date();

    T data;

    ApiError apiError;
}