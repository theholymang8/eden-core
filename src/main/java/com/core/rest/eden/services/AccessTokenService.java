package com.core.rest.eden.services;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccessTokenService {

    DecodedJWT decodeToken(String token);

    String extractUsername(String token);

    Date extractExpiration(String token);

    Map<String, Claim> extractClaims(String token);

    Boolean isTokenExpired(String token);

    String generateToken(User user, String issuer);

    String generateRefreshToken(User user, String issuer);

    String createToken(Map<String, List<String>> claims, String subject, String issuer);

    String createRefreshToken(Map<String, List<String>> claims, String subject, String issuer);

    Boolean validateToken(String token, String username);
}
