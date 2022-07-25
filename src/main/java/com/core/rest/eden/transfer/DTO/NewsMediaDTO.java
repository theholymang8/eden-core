package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsMediaDTO {

    @JsonProperty("url")
    private String url;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

}
