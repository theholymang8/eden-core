package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface PostService extends BaseService<Post, Long>{

    List<Post> findRecentPosts(Integer limit);

    List<Post> findUserPosts(User user, Integer limit);
}
