package com.core.rest.eden.transfer.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class EntitySentimentDTO {

    @JsonProperty(value = "Negative")
    private Float negative;

    @JsonProperty(value = "Neutral")
    private Float Neutral;

    @JsonProperty(value = "Positive")
    private Float Positive;



}
