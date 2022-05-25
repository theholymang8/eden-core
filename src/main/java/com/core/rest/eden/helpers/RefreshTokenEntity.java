package com.core.rest.eden.helpers;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@RedisHash("RefreshToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@SuperBuilder

public class RefreshTokenEntity implements Serializable {

    private Long id;

    private Date expirationDate;

    @Indexed
    private String refreshToken;
}
