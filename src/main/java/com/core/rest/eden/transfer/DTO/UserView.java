package com.core.rest.eden.transfer.DTO;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserView {

    @JsonView(Views.Public.class)
    @JsonProperty("id")
    private Long id;

    @JsonView(Views.Public.class)
    @JsonProperty("firstName")
    private String firstName;

    @JsonView(Views.Public.class)
    @JsonProperty("lastName")
    private String lastName;

    @JsonView(Views.Public.class)
    @JsonProperty("userName")
    private String userName;

    @JsonView(Views.Internal.class)
    @JsonProperty("accessToken")
    private String accessToken;

    @JsonView(Views.Internal.class)
    @JsonProperty("refreshToken")
    private String refreshToken;

    @JsonView(Views.Public.class)
    @JsonProperty("accessTokenExpiration")
    private Long accessTokenExpiration;

    @JsonView(Views.Public.class)
    @JsonProperty("refreshTokenExpiration")
    private Long refreshTokenExpiration;

}
