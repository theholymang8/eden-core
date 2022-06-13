package com.core.rest.eden.configuration.filter;

import com.core.rest.eden.services.AuthenticationService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.UserView;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;
    private final UserService userService;


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

        String accessToken = authenticationService.generateAccessToken(user, request.getRequestURL().toString());
        String refreshToken = authenticationService.generateRefreshToken(user, request.getRequestURL().toString());

        response.addCookie(authenticationService.generateAccessCookie(accessToken));
        response.addCookie(authenticationService.generateRefreshCookie(refreshToken));

        UserView authenticatedUser = userService.findByUsernameAuth(user.getUsername());
        authenticatedUser.setAccessTokenExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000).getTime());
        authenticatedUser.setRefreshTokenExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000).getTime());
        /*authenticatedUser.setAccessToken(accessToken);
        authenticatedUser.setRefreshToken(refreshToken);*/

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), authenticatedUser);

    }
}
