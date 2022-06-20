package com.core.rest.eden.transfer.projections;

import com.fasterxml.jackson.annotation.JsonProperty;


public interface FriendInterestsProjection {

    @JsonProperty("id")
    Long getId();

    @JsonProperty("title")
    String getTitle();

}
