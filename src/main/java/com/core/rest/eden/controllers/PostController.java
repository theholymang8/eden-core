package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.File;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.FileService;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.PostDTO;
import com.core.rest.eden.utility.TokenUtil;
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
    private final FileService fileService;
    private final UserService userService;

    @Override
    protected BaseService<Post, Long> getBaseService() {
        return postService;
    }

    @PostMapping(path = "upload")
    public ResponseEntity<ApiResponse<Post>> createWithImageDto(@Valid @RequestBody final PostDTO entity) {
        return ResponseEntity.ok(ApiResponse.<Post>builder()
                .data(postService.uploadPost(entity))
                .build());
    }

    /*@PostMapping(path = "upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Post>> createWithImage(@Valid @RequestPart("entity") final Post entity, @RequestPart("file") MultipartFile multipartFile, @RequestPart("username") String username) throws NullPointerException, IOException {



        User postUser = this.userService.findByUsername(username);
        entity.setUser(postUser);

        logger.info("Multi Part File: {}", multipartFile);
        File fileEntity = File.builder()
                .name(StringUtils.cleanPath(multipartFile.getOriginalFilename()))
                .contentType(multipartFile.getContentType())
                .data(multipartFile.getBytes())
                .build();


        postService.create(entity);

        fileEntity.setPost(entity);
        fileEntity.setUser(postUser);
        fileService.create(fileEntity);


        return new ResponseEntity<>(ApiResponse.<Post>builder().data(entity).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
    }

    @PostMapping(path = "upload",
    headers = "action=uploadSimple",
    consumes = "application/json")
    public ResponseEntity<ApiResponse<Post>> createSimple(@Valid @RequestBody final Post entity, @RequestPart("username") String username) {

        logger.info("Username is: {}", username);
        User postUser = this.userService.findByUsername(username);
        entity.setUser(postUser);

        postService.create(entity);

        return new ResponseEntity<>(ApiResponse.<Post>builder().data(entity).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
    }*/

    @GetMapping(path = "find",
                headers = "action=findRecentPosts",
                params = "limit")
    public ResponseEntity<ApiResponse<List<Post>>> findRecentPosts(@RequestParam(value = "limit", defaultValue = "10") Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(postService.findRecentPosts(limit))
                .build());
    }

    /*@GetMapping(path = "find",
            headers = "action=findTopicRelated",
            params = "limit")
    public ResponseEntity<ApiResponse<List<Post>>> findTopicRelatedPosts(@RequestParam(value = "limit", defaultValue = "10") Integer limit){
        return ResponseEntity.ok(ApiResponse.<List<Post>>builder()
                .data(postService.findRecentPosts(limit))
                .build());
    }*/

    @PutMapping(path = "like",
    params = {"postId"})
    public ResponseEntity<ApiResponse<Post>> addLike(@RequestParam(value = "postId") Long postId){
        return new ResponseEntity<>(ApiResponse.<Post>builder().data(postService.addLike(postId)).build(),
                getNoCacheHeaders(), HttpStatus.CREATED);
    }

    /*@PutMapping(path = "likev2")
    public void addLikeV2(@Valid @RequestBody final Post post){

        logger.info("Post is: {}", post);
        postService.addLikev2(post);
    }*/
}
