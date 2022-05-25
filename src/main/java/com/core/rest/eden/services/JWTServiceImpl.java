package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.helpers.RefreshTokenEntity;
import com.core.rest.eden.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl extends AbstractLogComponent implements JWTService{

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenEntity create(RefreshTokenEntity refreshTokenEntity) {
        logger.trace("Saving User's Refresh Token: {}", refreshTokenEntity);
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public void update(RefreshTokenEntity refreshTokenEntity) {
        logger.trace("Updating User's Refresh Token: {}", refreshTokenEntity);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public void delete(RefreshTokenEntity refreshTokenEntity) {
        logger.trace("Deleting User's Refresh Token {}.", refreshTokenEntity);
        refreshTokenRepository.delete(refreshTokenEntity);
    }

    @Override
    public void deleteById(Long id) {
        final RefreshTokenEntity foundRefreshTokenEntity = refreshTokenRepository.findById(id).orElseThrow(NoSuchElementException::new);
        logger.trace("Deleting User's Refresh Token {}.", foundRefreshTokenEntity);
        refreshTokenRepository.deleteById(id);
    }

    @Override
    public boolean exists(RefreshTokenEntity refreshTokenEntity) {
        logger.trace("Checking whether {} exists.", refreshTokenEntity);
        return refreshTokenRepository.existsById(refreshTokenEntity.getId());
    }

    @Override
    public RefreshTokenEntity find(Long id) {
        return refreshTokenRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public RefreshTokenEntity findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public List<RefreshTokenEntity> findAll() {
        return (List<RefreshTokenEntity>) refreshTokenRepository.findAll();
    }
}
