package com.core.rest.eden.controllers.transfer;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder
public class ApiError {

    Integer status;

    String message;

    String path;
}
