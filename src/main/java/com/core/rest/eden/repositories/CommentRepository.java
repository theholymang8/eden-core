package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.transfer.DTO.CommentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query
    List<CommentView> findCommentViews(Post post);



}
