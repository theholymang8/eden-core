package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.projections.FriendInterestsProjection;

import java.util.List;
import java.util.Set;

public interface TopicService extends BaseService<Topic, Long>{

    Topic findByTitle(String title);

    Set<Topic> findByUsers(List<User> users);

    void updateUsers(Set<Topic> topics, User user);

    List<FriendInterestsProjection> findFriendsInterests(Long userId);

}
