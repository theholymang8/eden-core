package com.core.rest.eden.services;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.CommentRepository;
import com.core.rest.eden.transfer.DTO.CommentView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    public JpaRepository<Comment, Long> getRepository() {
        return commentRepository;
    }

    @Override
    public List<CommentView> postComments(Post post) {
        return commentRepository.findCommentViews(post);
    }
}
