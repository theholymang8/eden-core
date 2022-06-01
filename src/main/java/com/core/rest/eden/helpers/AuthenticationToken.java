package com.core.rest.eden.helpers;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode


public class AuthenticationToken implements Serializable {

    @Value("${jwt_keyword}")
    private static String TOKEN_SECRET;

    private String accessToken;

    private String refreshToken;

    public static byte [] getTokenSecret() {
        return TOKEN_SECRET.getBytes();
    }
}
