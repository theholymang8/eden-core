package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.PostRepository;
import com.core.rest.eden.transfer.DTO.CommentView;
import com.core.rest.eden.transfer.DTO.UserPostView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService{

    private final PostRepository postRepository;

    private final CommentService commentService;

    @Override
    public JpaRepository<Post, Long> getRepository() {
        return postRepository;
    }

    @Override
    public List<Post> findRecentPosts(Integer limit) {
        return postRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "dateCreated"))).getContent();
    }

    @Override
    public List<Post> findUserPosts(User user, Integer limit) {
       return postRepository.findAllByUser(user, PageRequest.of(0,limit));
    }



}
