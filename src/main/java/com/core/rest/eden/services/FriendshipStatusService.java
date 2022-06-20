package com.core.rest.eden.services;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface FriendshipStatusService extends BaseService<FriendshipStatus, Long> {

    FriendshipStatus newFriendRequest(User requester, User addressee);

    FriendshipStatus acceptFriendRequest(User requester, User addressee);

    FriendshipStatus rejectFriendRequest(User requester, User addressee);

    FriendshipStatus deleteFriendRequest(User requester, User addressee);

    Boolean hasSentRequest(User requester, User addressee);

    FriendshipStatus blockFriendRequest(User requester, User addressee);

    List<User> getAllFriendRequests(User addressee);

    List<FriendshipStatus> findAllByStatus(Status status);

    List<FriendshipStatus> findAllBySpecifier(User user);
}
