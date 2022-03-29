package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    //@Query(value = "select u, p from User u " +
    //        "inner join fetch u.posts p " +
    //        "where u.firstName=?1 " +
    //        "and u.lastName=?2")
    @Query
    User findByFirstNameAndLastName(String firstName, String lastName);


}
