package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.File;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.*;
import com.core.rest.eden.transfer.DTO.PostDTO;
import com.core.rest.eden.transfer.views.Views;
import com.core.rest.eden.utility.TokenUtil;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostController extends AbstractController<Post>{

    private final PostService postService;
    private final PostBehaviourService postBehaviourService;

    @Override
    protected BaseService<Post, Long> getBaseService() {
        return postService;
    }

    @JsonView(Views.Public.class)
    @PostMapping(path = "/{postID}",
            headers = "action=addComment"
    )
    public ResponseEntity<ApiResponse<Post>> addComment(
            @PathVariable("postID") Long postID, @Valid @RequestBody Comment comment){
        return ResponseEntity.ok(ApiResponse.<Post>builder()
                .data(postService.addComment(postID, comment))
                .build());
    }

    @JsonView(Views.Public.class)
    @DeleteMapping(path = "/{postID}",
            headers = "action=deleteComment"
    )
    public ResponseEntity<ApiResponse<Post>> deleteComment(
            @PathVariable("postID") Long postID, @Valid @RequestBody Comment comment){
        return ResponseEntity.ok(ApiResponse.<Post>builder()
                .data(postService.deleteComment(postID, comment))
                .build());
    }


    @GetMapping(path = "find",
                headers = "action=findRecentPosts",
                params = "limit")
    public ResponseEntity<ApiResponse<List<Post>>> findRecentPosts(@RequestParam(value = "limit", defaultValue = "10") Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(postService.findRecentPosts(limit))
                .build());
    }


    @JsonView(Views.Detailed.class)
    @PutMapping(path = "like",
    params = {"postId", "userId"})
    public ResponseEntity<ApiResponse<Post>> addLike(@RequestParam(value = "postId") Long postId, @RequestParam(value="userId") Long userId){
        return new ResponseEntity<>(ApiResponse.<Post>builder().data(postBehaviourService.addLike(postId, userId)).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
    }

    @JsonView(Views.Detailed.class)
    @PutMapping(path = "dislike",
            params = {"postId", "userId"})
    public ResponseEntity<ApiResponse<Post>> addDislike(@RequestParam(value = "postId") Long postId, @RequestParam(value="userId") Long userId){
        return new ResponseEntity<>(ApiResponse.<Post>builder().data(postBehaviourService.addDislike(postId, userId)).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
    }

}
