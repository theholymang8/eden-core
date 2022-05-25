package com.core.rest.eden.repositories;

import com.core.rest.eden.helpers.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, Long> {

    RefreshTokenEntity findByRefreshToken(String refreshToken);

    //Delete the token via Redis
}
