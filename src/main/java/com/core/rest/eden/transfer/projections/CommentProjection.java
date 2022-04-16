package com.core.rest.eden.transfer.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public interface CommentProjection {

    @JsonProperty("body")
    String getBody();

    @JsonProperty("created at")
    LocalDateTime getDateCreated();

    @JsonProperty("updated at")
    LocalDateTime getDateUpdated();

    @JsonProperty("user")
    String getUserFirstNameAndLastName();

}
