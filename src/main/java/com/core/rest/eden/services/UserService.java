package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface UserService extends BaseService<User, Long>{

    User findByName(String firstName, String lastName);

    User findByUsername(String username);

    List<Post> findPosts(String firstName, String lastName, Integer limit);

    void addRoleToUser(String firstName, String lastName, Role role);

    User saveUser(User user);

    User loadUserByEmail(String username);

}
