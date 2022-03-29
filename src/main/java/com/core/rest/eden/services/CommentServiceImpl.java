package com.core.rest.eden.services;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public JpaRepository<Comment, Long> getRepository() {
        return commentRepository;
    }
}
