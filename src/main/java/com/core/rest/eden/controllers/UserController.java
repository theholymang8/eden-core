package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.exceptions.UserHasAlreadySentRequestException;
import com.core.rest.eden.services.AuthenticationService;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.FriendRequestDTO;
import com.core.rest.eden.transfer.DTO.PostDTO;
import com.core.rest.eden.transfer.DTO.UserRegisterDTO;
import com.core.rest.eden.transfer.DTO.UserView;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @JsonView(Views.Detailed.class)
    @GetMapping(
            headers = "action=addComment",
            params = {"firstName", "lastName"})
    public ResponseEntity<ApiResponse<User>> addComment(
            @Valid @RequestParam("firstName") String firstName, @Valid @RequestParam String lastName){
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(userService.findByName(firstName, lastName))
                .build());
    }


    @PostMapping(path = "upload")
    public ResponseEntity<ApiResponse<Post>> createWithImageDto(@Valid @RequestBody final PostDTO entity) {
        return ResponseEntity.ok(ApiResponse.<Post>builder()
                .data(userService.uploadPost(entity))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findPosts",
            params = {"firstName", "lastName", "limit", "page"})
    public ResponseEntity<ApiResponse<List<Post>>> findPosts(
            @Valid @RequestParam("firstName") String firstName,
            @Valid @RequestParam String lastName,
            @RequestParam Integer limit,
            @RequestParam(defaultValue = "0") Integer page){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findPosts(firstName, lastName, limit, page))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findFriends",
            params = {"userId"})
    public ResponseEntity<ApiResponse<List<User>>> findFriends(
            @Valid @RequestParam("userId") Long userId){
        return ResponseEntity.ok(ApiResponse.<List<User>>builder()
                .data(userService.findFriends(userId))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findFriendsPosts",
            params = {"username", "limit", "page"})
    public ResponseEntity<ApiResponse<List<Post>>> findPosts(
            @RequestParam(name = "username") String username,
            @RequestParam Integer limit,
            @RequestParam(defaultValue = "0") Integer page){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findFriendsPosts(username, limit, page))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            headers = "action=findTopicRelated",
            params = {"username", "limit", "page"})
    public ResponseEntity<ApiResponse<List<Post>>> findTopicRelatedPosts(
            @Valid @RequestParam("username") List<String> usernames,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer page){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findTopicRelatedPosts(usernames, limit, page))
                .build());
    }

    @JsonView(Views.Public.class)
    @PostMapping(
            headers = "action=findFriendship"
    )
    public ResponseEntity<ApiResponse<Friendship>> isAlreadyFriends(
            @Valid @RequestBody FriendRequestDTO friendRequestDTO
    ){
        return ResponseEntity.ok(ApiResponse.<Friendship>builder()
                .data(userService.isAlreadyFriends(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId()))
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "/{username}",
            headers = "action=findPostsByUsername",
            params = {"limit", "page"})
    public ResponseEntity<ApiResponse<List<Post>>> findPostsByUsername(
            @PathVariable("username") String username,
            @RequestParam Integer limit,
            @RequestParam(defaultValue = "0") Integer page){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(userService.findPostsByUsername(username, limit, page))
                .build());
    }

    @JsonView(Views.Detailed.class)
    @GetMapping(
            path = "find/{username}",
            headers = "action=findUserProfile"
    )
    public ResponseEntity<ApiResponse<User>> findUserProfile(
            @PathVariable("username") String username){
        return ResponseEntity.ok(ApiResponse.<User>builder()
                .data(userService.findUserProfile(username))
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


    @PostMapping(
            headers = "action=friendRequest"
    )
    public ResponseEntity<ApiResponse<String>> newFriendRequest(
            @Valid @RequestBody FriendRequestDTO friendRequestDTO
            )
    throws UserHasAlreadySentRequestException {
        userService.newFriendsRequest(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("A request to this user has been sent successfully")
                .build());
    }

    @JsonView(Views.Public.class)
    @GetMapping(
            path = "{userId}",
            headers = "action=getAllFriendRequests"
    )
    public ResponseEntity<ApiResponse<List<User>>> getFriendRequests(
            @PathVariable(name = "userId") Long userId
    )
            throws UserHasAlreadySentRequestException {
        //userService.newFriendsRequest(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId());
        return ResponseEntity.ok(ApiResponse.<List<User>>builder()
                .data(userService.getAllFriendRequests(userId))
                .build());
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            headers = "action=acceptRequest"
    )
    public void acceptFriendRequest(
            @Valid @RequestBody FriendRequestDTO friendRequestDTO
    ){
        userService.acceptFriendRequest(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(
            headers = "action=rejectRequest"
    )
    public void rejectFriendRequest(
            @Valid @RequestBody FriendRequestDTO friendRequestDTO
    ){
        userService.rejectFriendRequest(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(
            headers = "action=deleteFriendship"
    )
    public void deleteFriendship(
            @Valid @RequestBody FriendRequestDTO friendRequestDTO
    ){
        userService.deleteFriendship(friendRequestDTO.getRequesterId(), friendRequestDTO.getAddresseeId());
    }


}
