package com.core.rest.eden.services;

import com.core.rest.eden.helpers.RefreshTokenEntity;

import java.util.List;

public interface JWTService {

    RefreshTokenEntity create (RefreshTokenEntity refreshTokenEntity);

    void update(RefreshTokenEntity refreshTokenEntity);

    void delete(RefreshTokenEntity refreshTokenEntity);

    void deleteById(Long id);

    boolean exists(RefreshTokenEntity refreshTokenEntity);

    RefreshTokenEntity find(Long id);

    RefreshTokenEntity findByRefreshToken(String refreshToken);

    List<RefreshTokenEntity> findAll();
}
