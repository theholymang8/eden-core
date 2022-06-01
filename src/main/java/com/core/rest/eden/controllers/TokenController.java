package com.core.rest.eden.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.helpers.RefreshTokenEntity;
import com.core.rest.eden.services.AuthenticationService;
import com.core.rest.eden.services.JWTService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.UserView;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("token")
@Slf4j
public class TokenController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final JWTService jwtService;

    @JsonView(Views.Public.class)
    @GetMapping(
            path = "/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.validateCookie(request.getCookies(), "refresh-token")) {

            String providedToken = authenticationService.extractTokenFromCookie(request.getCookies(), "refresh-token");
            User user = userService.findByUsername(authenticationService.provideUser(providedToken));

            String accessToken = authenticationService.generateAccessToken(user, request.getRequestURL().toString());
            String refreshToken = authenticationService.generateRefreshToken(user, request.getRequestURL().toString());

            response.addCookie(authenticationService.generateAccessCookie(accessToken));
            response.addCookie(authenticationService.generateRefreshCookie(refreshToken));

            UserView authenticatedUser = userService.findByUsernameAuth(user.getUsername());
            authenticatedUser.setAccessToken(accessToken);
            authenticatedUser.setRefreshToken(refreshToken);

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), authenticatedUser);

        } else {
            response.setHeader("Authorization Error", "Refresh Token is missing");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Refresh Token is missing");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);

        }
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "/revoke")
    public void revokeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {


        if (authenticationService.authoriseUser(request.getHeader(AUTHORIZATION))) {

            response.addCookie(authenticationService.generateExpiredAccessCookie());
            response.addCookie(authenticationService.generateExpiredRefreshCookie());

            response.setStatus(OK.value());
            Map<String, String> result = new HashMap<>();
            result.put("Status", "Refresh Token Revoked");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), result);

        } else {

            response.setHeader("Authorization Error", "Refresh Token is missing");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Refresh Token is not valid");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
}
