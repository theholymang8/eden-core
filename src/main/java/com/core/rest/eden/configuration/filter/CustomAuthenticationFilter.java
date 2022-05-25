package com.core.rest.eden.configuration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.core.rest.eden.helpers.RefreshTokenEntity;
import com.core.rest.eden.services.JWTService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.UserView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor @Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    private final JWTService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();
        UserView authenticatedUser = userService.findByUsernameAuth(user.getUsername());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());


        String access_token = JWT.create()
                .withSubject(user.getUsername())
                /* 10 Minutes expiration date */
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("ip", getClientIp(request))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                /* 30 Minutes expiration date */
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Cookie cookie = new Cookie("jwt-token", access_token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10 * 60);
        Cookie refreshCookie = new Cookie("refresh-token", refresh_token);
        refreshCookie.setPath("/");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(10 * 1440);


        response.addCookie(cookie);
        response.addCookie(refreshCookie);
        /* Pass the tokens in the headers */
        //response.setHeader("access_token", access_token);
        //response.setHeader("refresh_token", refresh_token);

        /* Set Tokens to authenticated User Object DTO */
        authenticatedUser.setAccessToken(access_token);
        authenticatedUser.setRefreshToken(refresh_token);

        /* Persist User's Refresh Token */
        jwtService.create(RefreshTokenEntity.builder()
                .expirationDate(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .refreshToken(refresh_token)
                .build());

        log.info("Token just stored: {}", jwtService.findByRefreshToken(refresh_token));

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), authenticatedUser);

        //super.successfulAuthentication(request, response, chain, authResult);
    }

    /* Get Client's IP address */
    private static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
