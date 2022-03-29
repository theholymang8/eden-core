package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController extends AbstractController<Post>{

    private final PostService postService;

    @Override
    protected BaseService<Post, Long> getBaseService() {
        return postService;
    }

    @GetMapping(path = "find",
                headers = "action=findRecentPosts",
                params = "limit")
    public ResponseEntity<ApiResponse<List<Post>>> findRecentPosts(@RequestParam(value = "limit", defaultValue = "10") Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(postService.findRecentPosts(limit))
                .build());
    }
}
