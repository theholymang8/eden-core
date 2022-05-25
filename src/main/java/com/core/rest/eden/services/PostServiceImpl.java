package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
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

    @Override
    public List<Post> findByTopics(Set<Topic> topics, Integer limit) {
        return postRepository.findAllByTopicsIn(topics, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "dateCreated")));
    }

    /*@Override
    public List<Post> findFriendsPosts(User user, Integer limit) {
        return postRepository.findFriendsPosts(user, PageRequest.of(0, limit));
    }*/

    @Override
    public void addLike(Long id) {
        Post foundPost = postRepository.findById(id).orElseThrow(NoSuchElementException::new);
        foundPost.setLikes(foundPost.getLikes()+1);
        // logger.info("post has: {} likes", post.getLikes());
        postRepository.save(foundPost);
    }

    @Override
    public void addLikev2(Post post) {
        //Post foundPost = postRepository.findById(id).orElseThrow(NoSuchElementException::new);
        post.setLikes(post.getLikes()+1);
        // logger.info("post has: {} likes", post.getLikes());
        postRepository.save(post);
    }


}
