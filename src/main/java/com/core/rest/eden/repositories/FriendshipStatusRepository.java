package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {

    List<FriendshipStatus> findAllByFriendshipStatus(Status status);

    List<FriendshipStatus> findAllBySpecifier(User user);

}
