package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.transfer.DTO.PostDTO;
import com.core.rest.eden.transfer.DTO.UpdateSettingsDTO;
import com.core.rest.eden.transfer.DTO.UserRegisterDTO;
import com.core.rest.eden.transfer.DTO.UserView;
import com.core.rest.eden.transfer.projections.FriendInterestsProjection;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface UserService extends BaseService<User, Long>{

    List<User> findByTopics(Set<Topic> topics);

    List<Post> getRelatedPosts(String username, Integer limit, Integer page);

    User findByName(String firstName, String lastName);

    User findByUsername(String username);

    UserView findByUsernameAuth(String username);

    List<Post> findPosts(String firstName, String lastName, Integer limit, Integer page);

    List<Post> findPostsByUsername(String username, Integer limit, Integer page);

    void addRoleToUser(String firstName, String lastName, Role role);

    List<Post> findTopicRelatedPosts(List<String> usernames, Integer limit, Integer page);

    User saveUser(User user);

    User findUserProfile(String username);

    Post uploadPost(PostDTO entity) throws ExecutionException, InterruptedException;

    Post addComment(Post post, String username);

    User loadUserByEmail(String username);

    List<User> findFriends(Long userId);

    List<Post> findFriendsPosts(String username, Integer limit);

    UserView registerUser(UserRegisterDTO user, String requestUrl);

    void newFriendsRequest(Long requesterId, Long addresseeId);

    void acceptFriendRequest(Long requesterId, Long addresseeId);

    void rejectFriendRequest(Long requesterId, Long addresseeId);

    void deleteFriendship(Long requesterId, Long addresseeId);

    List<Post> findFriendsPosts(String username, Integer limit, Integer page);

    Friendship isAlreadyFriends(Long requesterId, Long addresseeId);

    Boolean hasSentFriendRequest(Long requesterId, Long addresseeId);

    List<User> getAllFriendRequests(Long addresseeId);

    void updateSettings(UpdateSettingsDTO newSettings);

    List<User> getRecommenderFriends(Long userId);


    /*List<FriendInterestsProjection> findFriendsInterest(Long userId);*/

}
