package com.core.rest.eden.configuration.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private AuthenticationService authenticationService;

    private Set<String> excludedUrlPatterns = new HashSet<>(Arrays.asList(
            "/token/revoke/",
            "/login",
            "/token/refresh/",
            "/topics/"
    ));
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (authenticationService.authoriseUser(request.getHeader(AUTHORIZATION))){
            filterChain.doFilter(request,response);
        }else {
            response.setHeader("Authorization Error", "Fake Token");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Not Authorized");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

       /* if(request.getServletPath().equals("/token/revoke/")){
            //log.info("Revoking Token");
            filterChain.doFilter(request, response);
        }
        if(request.getServletPath().equals("/login") || request.getServletPath().equals("/token/refresh/") || request.getServletPath().equals("/token/refresh/")){
            filterChain.doFilter(request, response);
        }else{
            String accessToken = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("jwt-token")) {
                    accessToken = cookie.getValue();
                }
                //log.info("Cookie name: {} and value: {}", cookie.getName(), cookie.getValue());
            }
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            //if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            if (accessToken != null && authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                *//* Pass the token and filter out the word Bearer *//*
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    if (token.equals(accessToken)){
                        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                        JWTVerifier verifier = JWT.require(algorithm).build();
                        DecodedJWT decodedJWT = verifier.verify(accessToken);
                        String username = decodedJWT.getSubject();
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    }else{
                        response.setHeader("Authorization Error", "Fake Token");
                        response.setStatus(FORBIDDEN.value());
                        Map<String, String> error = new HashMap<>();
                        error.put("error_message", "Not Authorized");
                        response.setContentType(APPLICATION_JSON_VALUE);
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
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
                filterChain.doFilter(request, response);
            }*/
        }
        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            return excludedUrlPatterns.stream().anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
        }
    }




