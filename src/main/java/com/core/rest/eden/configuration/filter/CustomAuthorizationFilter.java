package com.core.rest.eden.configuration.filter;

import com.core.rest.eden.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    private final Set<String> excludedUrlPatterns = new HashSet<>(Arrays.asList(
            "/token/revoke/",
            "/users/register",
            "/login",
            "/token/refresh/",
            "/topics"
    ));
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (authenticationService.authoriseUser(request.getHeader(AUTHORIZATION))){
            filterChain.doFilter(request,response);
        }else {
            response.setHeader("Authorization Error", "Not Authorised");
            response.setStatus(FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", "Not Authorized");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedUrlPatterns.stream().anyMatch(url -> pathMatcher.match(url, request.getServletPath()));
    }
}




