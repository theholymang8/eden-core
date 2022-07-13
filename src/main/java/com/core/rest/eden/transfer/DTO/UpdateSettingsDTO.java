package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UpdateSettingsDTO {

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "newUsername")
    private String newUsername;

    @JsonProperty(value = "newEmail")
    private String newEmail;

    @JsonProperty(value = "newTopics")
    private Set<String> newTopics;

}
