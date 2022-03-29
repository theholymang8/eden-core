package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUser(User user, Pageable pageable);

}
