package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.DTO.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @Query(value = "select distinct u from User u left join fetch u.topics left join fetch u.files left join fetch u.posts")
    List<User> findAll();

    @Query
    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = "select u from User u left join fetch u.topics where u.username = :username")
    User findUserProfile(String username);

    @Query(value = "select new com.core.rest.eden.transfer.DTO.UserView(u.id, u.firstName, u.lastName, u.username, u.email, u.gender, '', '', 0L, 0L) from User u where u.username = :username")
    UserView findByUsernameAuth(String username);

    @Query(value = "select f.addressee from Friendship f where f.requester=:user")
    List<User> findFriends(User user);

}
