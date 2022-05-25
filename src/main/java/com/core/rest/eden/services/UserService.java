package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.DTO.UserView;

import java.util.List;

public interface UserService extends BaseService<User, Long>{

    User findByName(String firstName, String lastName);

    User findByUsername(String username);

    UserView findByUsernameAuth(String username);

    List<Post> findPosts(String firstName, String lastName, Integer limit);

    List<Post> findPostsByUsername(String username, Integer limit);

    void addRoleToUser(String firstName, String lastName, Role role);

    List<Post> findTopicRelatedPosts(List<String> usernames, Integer limit);

    User saveUser(User user);

    User loadUserByEmail(String username);

    List<User> findFriends(User user);

    List<Post> findFriendsPosts(String username, Integer limit);

    //List<Post> findFriendsPostsPageable(String username, Integer limit);

}
