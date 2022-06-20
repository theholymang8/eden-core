package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.projections.FriendInterestsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class TopicController extends AbstractController<Topic>{

    private final TopicService topicService;
    private final UserService userService;

    @Override
    protected BaseService<Topic, Long> getBaseService() {
        return topicService;
    }

    @GetMapping(path = "find", headers = "action=findByUsers")
    public ResponseEntity<ApiResponse<Set<Topic>>> findByUsers(@RequestParam List<String> users){
        List<User> userList = new ArrayList<>();
        users.forEach(username -> userList.add(userService.findByUsername(username)));
        return ResponseEntity.ok(ApiResponse.<Set<Topic>>builder()
                .data(topicService.findByUsers(userList))
                .build());
    }

    @GetMapping(path = "find", headers = "action=findByTitle")
    public ResponseEntity<ApiResponse<Topic>> findByTitle(@RequestParam String title){
        return ResponseEntity.ok(ApiResponse.<Topic>builder()
                .data(topicService.findByTitle(title))
                .build());
    }

    @GetMapping(
            path = "find",
            headers = "action=findFriendsInterests",
            params = {"userId"}
    )
    public ResponseEntity<ApiResponse<List<FriendInterestsProjection>>> findFriendsInterests(@RequestParam Long userId){
        return ResponseEntity.ok(ApiResponse.<List<FriendInterestsProjection>>builder()
                .data(topicService.findFriendsInterests(userId))
                .build());
    }
}
