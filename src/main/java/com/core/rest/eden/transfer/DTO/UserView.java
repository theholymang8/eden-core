package com.core.rest.eden.transfer.DTO;

import com.core.rest.eden.domain.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserView {

    @JsonProperty("name")
    private String firstNameAndLastName;

    @JsonProperty("date of birth")
    private LocalDate dateOfBirth;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private Gender gender;
}
