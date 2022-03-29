package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

    private final UserRepository userRepository;

    private final PostService postService;

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public User findByName(String firstName, String lastName) {

        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);

        if(!checkNullability(user)){
            return user;
        }else{
            return null;
        }

    }

    @Override
    public List<Post> findPosts(String firstName, String lastName, Integer limit) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return postService.findUserPosts(user, limit);
    }


}
