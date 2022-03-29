package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController extends AbstractController<User>{

    private final UserService userService;

    @Override
    protected BaseService<User, Long> getBaseService() {
        return userService;
    }

    @GetMapping(
            headers = "action=findByName",
            params = {"firstName", "lastName"})
    public ResponseEntity<ApiResponse<User>> findByName(
            @Valid @RequestParam("firstName") String firstName, @Valid @RequestParam String lastName){
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(userService.findByName(firstName, lastName))
                .build());
    }

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

}
