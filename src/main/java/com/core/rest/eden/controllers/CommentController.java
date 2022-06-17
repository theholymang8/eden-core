package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.CommentService;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
public class CommentController extends AbstractController<Comment>{

    private final CommentService commentService;

    @Override
    protected BaseService<Comment, Long> getBaseService() {
        return commentService;
    }

}
