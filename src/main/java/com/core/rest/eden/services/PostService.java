package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.transfer.DTO.PostDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostService extends BaseService<Post, Long>{


    Post findById(Long id);

    List<Post> findByClusteredTopic(Integer clusteredTopic, Integer limit, Integer page);

    List<Post> findRecentPosts(Integer limit);

    List<Post> findUserPosts(User user, Integer limit, Integer page);

    Long countPostsByUser(User user);

    Long countUserPostsByTopic(User user, Topic topic);

    Long countUserPostsBySentimentAndTopic(User user, Topic topic, Sentiment sentiment);

    List<Post> findByTopics(Set<Topic> topics, User user, Integer limit, Integer page);

    Post addLike(Long id);

    Post addComment(Long postID, Comment comment);

    Post deleteComment(Long postID, Comment comment);

    List<Post> findFriendsPosts(User user, Integer limit, Integer page);

    List<Integer> findClusters();

    /*Post uploadPost(PostDTO postDTO);*/

    void addLikev2(Post post);
}
