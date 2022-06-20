package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {

    List<FriendshipStatus> findAllByFriendshipStatus(Status status);

    List<FriendshipStatus> findAllBySpecifier(User user);

    List<FriendshipStatus> findAllByAddresseeAndFriendshipStatus(User addressee, Status status);

    @Query(value = "select fs.requester from FriendshipStatus fs where fs.addressee = :addressee and fs.friendshipStatus not in (:statuses)")
    List<User> findAllFriendRequests(User addressee, List<Status> statuses);

    FriendshipStatus findByRequesterAndAddresseeAndFriendshipStatus(User requester, User addressee, Status status);

}
