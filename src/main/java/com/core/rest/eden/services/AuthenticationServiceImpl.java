package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;


import com.core.rest.eden.transfer.DTO.UserView;
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

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl extends AbstractLogComponent implements AuthenticationService {


    private final UserView userView;
    private final AccessTokenService accessTokenService;
    private String issuer;

    public String extractToken(String authHeader) throws IndexOutOfBoundsException{
        return authHeader.substring("Bearer ".length());
    }

    @Override
    public Boolean validateToken(String authHeader){
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
    public Boolean validateCookie(Cookie[] cookies, String value) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(value)) {
                    return true;
                }
            }
        }
        logger.warn("Empty Cookies on request");
        return false;
    }

    @Override
    public Cookie generateAccessCookie(@NotNull User user, String issuer) {
        return new Cookie("access-token", accessTokenService.generateToken(user, issuer)) {{
            setMaxAge(10 * 60);
            setHttpOnly(true);
            setPath("/");
        }};
    }

    @Override
    public Cookie generateRefreshCookie(User user, String issuer) {
        return new Cookie("access-token", accessTokenService.generateRefreshToken(user, issuer)) {{
            setMaxAge(30 * 60);
            setHttpOnly(true);
            setPath("/");
        }};
    }
}
