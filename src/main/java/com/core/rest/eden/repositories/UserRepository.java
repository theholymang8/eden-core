package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface UserRepository extends JpaRepository<User, Long> {

    @Query
    User findByFirstNameAndLastName(String firstName, String lastName);

    User findByEmail(String email);

    User findByUsername(String username);

}
