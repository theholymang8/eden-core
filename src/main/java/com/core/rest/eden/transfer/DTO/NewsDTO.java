package com.core.rest.eden.transfer.DTO;

import com.core.rest.eden.configuration.JsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class NewsDTO {

    @JsonProperty("abstract")
    private String abstractNew;

    @JsonProperty("web_url")
    private String webUrl;

    @JsonProperty("snippet")
    private String snippet;

    @JsonProperty("lead_paragraph")
    private String leadParagraph;

    @JsonProperty("source")
    private String source;

    private String topic;

    @JsonProperty("multimedia")
    private List<NewsMediaDTO> multimedia;

    @JsonProperty("pub_date")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date published;

}
