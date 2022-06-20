package com.core.rest.eden.services;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface FriendshipService extends BaseService<Friendship, Long> {

    Friendship acceptFriendship(User requester, User addressee);

    Friendship isAlreadyFriends(User requester, User addressee);

    Boolean hasSentRequest(User requester, User addressee);

    void requestFriendship(User requester, User addressee);

    void rejectFriendship(User requester, User addressee);

    void deleteFriendship(User requester, User addressee);

    List<User> getAllFriendRequests(User addressee);

    List<Friendship> findAllByRequester(User user);

    List<Friendship> findAllByAddressee(User user);
}
