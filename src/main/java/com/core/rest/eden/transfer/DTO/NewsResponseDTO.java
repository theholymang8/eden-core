package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewsResponseDTO {

    @JsonProperty("status")
    private String status;

    @JsonProperty("copyright")
    private String copyright;

    @JsonProperty("response")
    private NewsDocsDTO response;

}
