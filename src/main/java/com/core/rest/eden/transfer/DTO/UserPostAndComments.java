package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPostAndComments {

    @JsonProperty("user")
    private String postUserFirstNameAndLastName;

    @JsonIgnore
    private Long postID;

    @JsonProperty("created at")
    private LocalDateTime dateCreated;

    @JsonProperty("updated at")
    private LocalDateTime dateUpdated;

    @JsonProperty("body")
    private String postBody;

    @JsonProperty("likes")
    private Integer postLikes;

    @JsonProperty("comments")
    private List<CommentView> comments;
}
