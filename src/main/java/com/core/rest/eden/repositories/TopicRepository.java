package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.projections.FriendInterestsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByTitle(String title);

    List<Topic> findAllByUsers(User user);

    /*@Query(value = "select distinct t from Topic t left outer join Friendship f on f.addressee = (select u from User u ) where f.requester = :user")
    List<Topic> findFriendsInterests(User user);*/

    @Query(nativeQuery = true)
    List<FriendInterestsProjection> findFriendsInterests(Long userId);
}
