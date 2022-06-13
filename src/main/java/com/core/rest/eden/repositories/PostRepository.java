package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    /*@Override
    @Query(value = "select p from Post p join fetch p.files  join fetch p.topics  join fetch p.user where p.id = :id")
    Optional<Post> findById(Long id);*/

    List<Post> findAllByUser(User user, Pageable pageable);

    Post findByUser(User user);

    List<Post> findDistinctAllByTopicsIn(Set<Topic> topics, Pageable pageable);
}
