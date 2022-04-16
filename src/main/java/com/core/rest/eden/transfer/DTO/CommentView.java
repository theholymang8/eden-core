package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentView {

    @JsonProperty("body")
    private String body;

    @JsonProperty("created at")
    private LocalDateTime dateCreated;

    @JsonProperty("updated at")
    private LocalDateTime dateUpdated;

    @JsonProperty("user")
    private String userFirstNameAndLastName;

}
