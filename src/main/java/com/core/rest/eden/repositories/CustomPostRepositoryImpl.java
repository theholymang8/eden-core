package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post findByIdCustom(Long id) {
        Post post = entityManager
                .createQuery("select p from Post p left join fetch p.topics left join fetch p.comments where p.id = :id", Post.class)
                .setParameter("id", id)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
                .getSingleResult();



        //log.info("Getting post: {}", post);
        return post;
    }
}
