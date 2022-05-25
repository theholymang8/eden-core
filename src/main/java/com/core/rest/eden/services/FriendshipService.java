package com.core.rest.eden.services;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.User;

import java.util.List;

public interface FriendshipService extends BaseService<Friendship, Long> {

    List<Friendship> findAllByRequester(User user);

    List<Friendship> findAllByAddressee(User user);
}
