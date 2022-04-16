package com.core.rest.eden.transfer.projections;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public interface UserPostProjection {

    @JsonProperty("full name")
    String getUserFullName();

    @JsonProperty("created at")
    LocalDateTime getDateCreated();

    @JsonProperty("updated at")
    LocalDateTime getDateUpdated();

    @JsonProperty("body")
    String getBody();

    @JsonProperty("likes")
    Integer getLikes();

    @JsonProperty("comments")
    List<CommentProjection> getComments();



}
