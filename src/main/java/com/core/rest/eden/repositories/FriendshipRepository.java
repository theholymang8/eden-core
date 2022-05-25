package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findAllByRequester(User user);

    List<Friendship> findAllByAddressee(User user);

}
