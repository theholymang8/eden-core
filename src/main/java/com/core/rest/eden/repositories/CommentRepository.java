package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
