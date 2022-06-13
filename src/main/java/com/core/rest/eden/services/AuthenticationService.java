package com.core.rest.eden.services;

import com.core.rest.eden.transfer.DTO.UserView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.Cookie;
import java.util.Collection;

public interface AuthenticationService {

    Cookie generateExpiredAccessCookie();

    Cookie generateExpiredRefreshCookie();

    String generateAccessToken(User user, String issuer);

    String generateRefreshToken(User user, String issuer);

    String generateAccessToken(com.core.rest.eden.domain.User user, String issuer);

    String generateRefreshToken(com.core.rest.eden.domain.User user, String issuer);

    String extractToken(String authHeader);

    Boolean authoriseUser(String authHeader);

    Boolean authoriseUser(Cookie [] cookies, String value);

    String extractTokenFromCookie(Cookie[] cookies, String value);

    String provideUser(String token);

    Boolean validateCookie(Cookie[] cookies, String value);

    Cookie generateAccessCookie(String accessToken);

    Cookie generateRefreshCookie(String refreshToken);

    Cookie generateAccessCookieExpiration();

    Cookie generateRefreshCookieExpiration();

    Boolean validateToken(String authHeader);

    Collection<SimpleGrantedAuthority> grantAuthorities(String token);
}
