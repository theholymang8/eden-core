package com.core.rest.eden.controllers;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
