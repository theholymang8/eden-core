package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.UserRegisterDTO;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController extends AbstractController<User>{

    private final UserService userService;

    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @PostMapping("/register")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody final UserRegisterDTO entity) {
        return new ResponseEntity<>(ApiResponse.<User>builder().data(userService.registerUser(entity)).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
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

    /*@JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findTopicRelated",
            params = {"username", "limit"})
    public ResponseEntity<ApiResponse<List<Post>>> findTopicRelatedPosts(
            @Valid @RequestParam("username") List<String> usernames,
            @RequestParam Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findTopicRelatedPosts(usernames, limit))
                .build());
    }*/

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findPostsByUsername",
            params = {"username", "limit"})
    public ResponseEntity<ApiResponse<List<Post>>> findPostsByUsername(
            @Valid @RequestParam("username") String username,
            @RequestParam Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findPostsByUsername(username, limit))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findFriendsPosts",
            params = {"username", "limit"})
    public ResponseEntity<ApiResponse<List<Post>>> findFriendsPosts(
            @Valid @RequestParam("username") String username,
            @RequestParam("limit") Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findFriendsPosts(username, limit))
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
