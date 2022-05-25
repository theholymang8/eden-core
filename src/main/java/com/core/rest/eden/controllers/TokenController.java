package com.core.rest.eden.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.helpers.RefreshTokenEntity;
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

    private final JWTService jwtService;

    @JsonView(Views.Public.class)
    @GetMapping(
            path = "/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //String authorizationHeader = request.getHeader(AUTHORIZATION);
        String refresh_token = null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refresh-token")) {
                refresh_token = cookie.getValue();
            }
            log.info("Cookie name: {} and value: {}", cookie.getName(), cookie.getValue());
        }



        if (refresh_token != null) {

            log.info("Token is: {}", refresh_token);
            /* Pass the token and filter out the word Bearer */
            try {
                //String auth_refresh_token = authorizationHeader.substring("Bearer ".length());
                /* If Token from cookie and header match */
                //if (auth_refresh_token.equals(refresh_token)) {

                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT decodedJWT = verifier.verify(refresh_token);
                    String username = decodedJWT.getSubject();

                    /* Check whether Redis has persisted the claimed refresh Token */
                    if (jwtService.findByRefreshToken(refresh_token) != null) {
                        User user = userService.findByUsername(username);
                        UserView authenticatedUser = userService.findByUsernameAuth(username);

                        String access_token = JWT.create()
                                .withSubject(user.getUsername())
                                /* 10 Minutes expiration date */
                                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                                .withIssuer(request.getRequestURL().toString())
                                .withClaim("roles", user.getRoles().stream().map(Role::toString).collect(Collectors.toList()))
                                .sign(algorithm);

                        authenticatedUser.setAccessToken(access_token);
                        authenticatedUser.setRefreshToken(refresh_token);

                        Cookie cookie = new Cookie("jwt-token", access_token);
                        cookie.setPath("/");
                        cookie.setHttpOnly(true);
                        cookie.setMaxAge(10 * 60);
                        Cookie refreshCookie = new Cookie("refresh-token", refresh_token);
                        refreshCookie.setPath("/");
                        refreshCookie.setHttpOnly(true);
                        refreshCookie.setMaxAge(10 * 1440);
                        //response.addHeader("Access-Control-Allow-Credentials", "true");
                        response.addCookie(cookie);
                        response.addCookie(refreshCookie);


                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), authenticatedUser);

                    /*} else {
                        response.setHeader("Authorization Error", "Refresh Token is not valid");
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", "Refresh Token is not valid");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    }
*/

                }

            }catch (Exception authenticationException){
                response.setHeader("Authorization Error", authenticationException.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", authenticationException.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }

        }else{
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
    public void revokeToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String access_token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("jwt-token")) {
                access_token = cookie.getValue();
            }
            //log.info("Cookie name: {} and value: {}", cookie.getName(), cookie.getValue());
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && access_token != null){

            String auth_access_token = authorizationHeader.substring("Bearer ".length());
            //log.info(auth_access_token);


            if (auth_access_token.equals(access_token)) {
                //if (jwtService.findByRefreshToken(access_token) != null) {
                    //log.info("I exist");
                    //Long refreshTokenID = jwtService.findByRefreshToken(access_token).getId();
                    //jwtService.deleteById(refreshTokenID);

                    Cookie cookie = new Cookie("jwt-token", null);
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge(0);
                    Cookie refreshCookie = new Cookie("refresh-token", null);
                    refreshCookie.setPath("/");
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setMaxAge(0);


                    response.addCookie(cookie);
                    response.addCookie(refreshCookie);

                    response.setStatus(OK.value());
                    Map<String, String> result = new HashMap<>();
                    result.put("Status", "Refresh Token Revoked");
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), result);
                //}
            }else{
                response.setHeader("Authorization Error", "Fake Refresh Token");
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", "Refresh Token is not valid");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
            //jwtService.findAll().forEach(token -> jwtService.deleteById(token.getId()));
            // jwtService.findAll().forEach(token -> log.info("Token: {}", token));


        }else{
            response.setHeader("Authorization Error", "Refresh Token is missing");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Refresh Token is not valid");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

}
