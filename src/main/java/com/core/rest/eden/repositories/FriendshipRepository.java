package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findAllByRequester(User user);

    List<Friendship> findAllByAddressee(User user);

    Friendship findByRequesterAndAddressee(User requester, User addressee);

    @Query(value = "select f from Friendship f where (f.requester= :requester and f.addressee= :addressee) or (f.requester = :addressee and f.addressee = :requester)")
    Friendship isAlreadyFriends(User requester, User addressee);
}
