package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface UserService extends BaseService<User, Long>{

    User findByName(String firstName, String lastName);

    List<Post> findPosts(String firstName, String lastName, Integer limit);
}
