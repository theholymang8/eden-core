package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PostService postService;

    private final PasswordEncoder passwordEncoder;

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
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Post> findPosts(String firstName, String lastName, Integer limit) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return postService.findUserPosts(user, limit);
    }

    @Override
    public void addRoleToUser(String firstName, String lastName, Role role) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        user.getRoles().add(role);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User loadUserByEmail(String username) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            logger.warn("User is null");
            throw new UsernameNotFoundException("User not found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
