package com.core.rest.eden.services;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.Cookie;
import java.util.Collection;

public interface AuthenticationService {

    String extractToken(String authHeader);

    Boolean authoriseUser(String authHeader);

    Boolean validateCookie(Cookie[] cookies, String value);

    Cookie generateAccessCookie(User user, String issuer);

    Cookie generateRefreshCookie(User user, String issuer);

    Boolean validateToken(String authHeader);

    Collection<SimpleGrantedAuthority> grantAuthorities(String token);
}
