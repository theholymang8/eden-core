package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;

import java.util.List;
import java.util.Set;

public interface PostService extends BaseService<Post, Long>{

    List<Post> findRecentPosts(Integer limit);

    List<Post> findUserPosts(User user, Integer limit);

    List<Post> findByTopics(Set<Topic> topics, Integer limit);

    void addLike(Long id);

    void addLikev2(Post post);
}
