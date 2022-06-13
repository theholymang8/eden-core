package com.core.rest.eden.controllers;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.AuthenticationService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.UserRegisterDTO;
import com.core.rest.eden.transfer.DTO.UserView;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthenticationController extends AbstractLogComponent {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    @JsonView(Views.Public.class)
    public ResponseEntity<ApiResponse<UserView>> create(@Valid @RequestBody final UserRegisterDTO entity,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response) {

        logger.info("Entity is :{}", entity);
        UserView registeredUser = userService.registerUser(entity, request.getRequestURL().toString());

        response.addCookie(authenticationService.generateAccessCookie(registeredUser.getAccessToken()));
        response.addCookie(authenticationService.generateRefreshCookie(registeredUser.getRefreshToken()));

        registeredUser.setAccessToken("");
        registeredUser.setRefreshToken("");



        /*logger.info("Registered User: {}", registeredUser);
        ResponseEntity<ApiResponse<UserView>> responseEntity = ResponseEntity.ok(ApiResponse.<UserView>builder().data(registeredUser).build());
        logger.info("Response Entity: {}", responseEntity);*/
        return ResponseEntity.ok(ApiResponse.<UserView>builder().data(registeredUser).build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            path = "token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (authenticationService.validateCookie(request.getCookies(), "refresh-token")) {

            String providedToken = authenticationService.extractTokenFromCookie(request.getCookies(), "refresh-token");
            User user = userService.findByUsername(authenticationService.provideUser(providedToken));

            String accessToken = authenticationService.generateAccessToken(user, request.getRequestURL().toString());
            String refreshToken = authenticationService.generateRefreshToken(user, request.getRequestURL().toString());

            response.addCookie(authenticationService.generateAccessCookie(accessToken));
            response.addCookie(authenticationService.generateRefreshCookie(refreshToken));

            UserView authenticatedUser = userService.findByUsernameAuth(user.getUsername());
            authenticatedUser.setAccessTokenExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000).getTime());
            authenticatedUser.setRefreshTokenExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000).getTime());

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
    @GetMapping(path = "token/revoke")
    public void revokeToken(HttpServletRequest request, HttpServletResponse response) throws IOException {


        if (authenticationService.authoriseUser(request.getCookies(), "access-token")) {

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
