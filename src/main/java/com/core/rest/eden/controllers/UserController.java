package com.core.rest.eden.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController extends AbstractController<User>{

    private final UserService userService;

    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @JsonView(Views.Detailed.class)
    @GetMapping(
            headers = "action=findByName",
            params = {"firstName", "lastName"})
    public ResponseEntity<ApiResponse<User>> findByName(
            @Valid @RequestParam("firstName") String firstName, @Valid @RequestParam String lastName){
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(userService.findByName(firstName, lastName))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findPosts",
            params = {"firstName", "lastName", "limit"})
    public ResponseEntity<ApiResponse<List<Post>>> findPosts(
            @Valid @RequestParam("firstName") String firstName,
            @Valid @RequestParam String lastName,
            @RequestParam Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findPosts(firstName, lastName, limit))
                .build());
    }



    @PostMapping(
            headers = "action=addRole",
            params = {"firstName", "lastName", "role"})
    public ResponseEntity<ApiResponse<User>> addRoleToUser(
            @Valid @RequestParam("firstName") String firstName,
            @Valid @RequestParam String lastName,
            @RequestParam Role role){
        userService.addRoleToUser(firstName, lastName, role);
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(userService.findByName(firstName, lastName))
                .build());
    }


}
