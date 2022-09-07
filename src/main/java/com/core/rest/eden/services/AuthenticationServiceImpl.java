package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;


import com.core.rest.eden.transfer.DTO.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl extends AbstractLogComponent implements AuthenticationService {

    private final AccessTokenService accessTokenService;


    @Override
    public Cookie generateExpiredAccessCookie() {
        return new Cookie("access-token", null) {{
            setMaxAge(0);
            setHttpOnly(true);
            setPath("/");
        }};
    }

    @Override
    public Cookie generateExpiredRefreshCookie() {
        return new Cookie("refresh-token", null) {{
            setMaxAge(0);
            setHttpOnly(true);
            setPath("/");
        }};
    }

    @Override
    public String generateAccessToken(@NotNull User user, String issuer) {
        return accessTokenService.generateToken(user, issuer);
    }

    @Override
    public String generateRefreshToken(@NotNull User user, String issuer) {
        return accessTokenService.generateRefreshToken(user, issuer);
    }

    @Override
    public String generateAccessToken(com.core.rest.eden.domain.User user, String issuer) {
        return accessTokenService.generateToken(user, issuer);
    }

    @Override
    public String generateRefreshToken(com.core.rest.eden.domain.User user, String issuer) {
        return accessTokenService.generateRefreshToken(user, issuer);
    }

    @Override
    public String extractToken(String authHeader) throws IndexOutOfBoundsException{
        return authHeader.substring("Bearer ".length());
    }

    @Override
    public Boolean validateToken(String authHeader) throws NullPointerException {
        if (!authHeader.startsWith("Bearer ")) return false;

        String token = extractToken(authHeader);
        String username = accessTokenService.extractUsername(token);
        return accessTokenService.validateToken(token, username);
    }

    @Override
    public Collection<SimpleGrantedAuthority> grantAuthorities(String token) throws NullPointerException, UnsupportedOperationException {
        String[] roles = accessTokenService.extractClaims(token).get("roles").asArray(String.class);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles)
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        return authorities;


    }

    @Override
    public Boolean authoriseUser(String authHeader) {
        if (!validateToken(authHeader)) return false;
        String token = extractToken(authHeader);
        Collection<SimpleGrantedAuthority> authorities = grantAuthorities(token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accessTokenService.extractUsername(token), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return true;
    }

    @Override
    public Boolean authoriseUser(Cookie[] cookies, String value) {
        String token = extractTokenFromCookie(cookies, value);
        String username = accessTokenService.extractUsername(token);
        if (!accessTokenService.validateToken(token, username)) return false;
        Collection<SimpleGrantedAuthority> authorities = grantAuthorities(token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accessTokenService.extractUsername(token), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return true;
    }

    @Override
    public String extractTokenFromCookie(Cookie[] cookies, String value) {
        if (!validateCookie(cookies, value)) return null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(value)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public String provideUser(String token) {
        return accessTokenService.extractUsername(token);
    }

    @Override
    public Boolean validateCookie(Cookie[] cookies, String value) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(value)) {
                    if (accessTokenService.validateToken(cookie.getValue(), accessTokenService.extractUsername(cookie.getValue()))) {
                        return true;
                    }
                }
            }
        }
        logger.warn("Empty Cookies on request");
        return false;
    }

    @Override
    public Cookie generateAccessCookie(String accessToken) {
        return new Cookie("access-token", accessToken) {{
            setMaxAge(10 * 60);
            setHttpOnly(true);
            setPath("/");
        }};
    }

    @Override
    public Cookie generateRefreshCookie(String refreshToken) {
        return new Cookie("refresh-token", refreshToken) {{
            setMaxAge(30 * 60);
            setHttpOnly(true);
            setPath("/");
        }};
    }

    @Override
    public Cookie generateAccessCookieExpiration() {
        return new Cookie("access-token-expiration", Integer.toString(10*60)) {{
            setPath("/");
        }};
    }

    @Override
    public Cookie generateRefreshCookieExpiration() {
        return new Cookie("refresh-token-expiration", Integer.toString(30*60)) {{
            setPath("/");
        }};
    }
}
