package com.core.rest.eden.services;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface FriendshipStatusService extends BaseService<FriendshipStatus, Long> {

    List<FriendshipStatus> findAllByStatus(Status status);

    List<FriendshipStatus> findAllBySpecifier(User user);
}
