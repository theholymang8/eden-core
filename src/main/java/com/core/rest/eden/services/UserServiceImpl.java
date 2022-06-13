package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.exceptions.UserAlreadyExistsException;
import com.core.rest.eden.repositories.UserRepository;
import com.core.rest.eden.transfer.DTO.UserRegisterDTO;
import com.core.rest.eden.transfer.DTO.UserView;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final PostService postService;
    private final AuthenticationService authenticationService;
    private final FileService fileService;

    //private final BCryptPasswordEncoder bCryptPasswordEncoder;
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

    @Transactional
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserView findByUsernameAuth(String username) {
        return userRepository.findByUsernameAuth(username);
    }

    @Override
    public List<Post> findPosts(String firstName, String lastName, Integer limit) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return postService.findUserPosts(user, limit);
    }

    @Override
    public List<Post> findPostsByUsername(String username, Integer limit) {
        User user = userRepository.findByUsername(username);
        return postService.findUserPosts(user, limit);
    }

    @Override
    public void addRoleToUser(String firstName, String lastName, Role role) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        user.getRoles().add(role);
    }

    @Override
    public List<Post> findTopicRelatedPosts(List<String> usernames, Integer limit) {
        List<User> users = new ArrayList<>();
        usernames.forEach(username -> users.add(userRepository.findByUsername(username)));
        Set<Topic> userRelatedTopics = topicService.findByUsers(users);
        List<Post> relatedPosts = postService.findByTopics(userRelatedTopics, limit);
        //relatedPosts.forEach(post -> logger.info("Post: {}", post.getId()));
        return postService.findByTopics(userRelatedTopics, limit);
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
    public List<User> findFriends(User user) {
        return userRepository.findFriends(user);
    }

    @Override
    public List<Post> findFriendsPosts(String username, Integer limit) {
        List<User> friends = this.findFriends(this.findByUsername(username));
        List<Post> friendsPosts = new ArrayList<>();
        friends.forEach(friend -> friendsPosts.addAll(friend.getPosts()));

        limit = (friendsPosts.size() >= limit) ? limit : friendsPosts.size();

        return friendsPosts.stream()
                .sorted(Comparator.comparing(Post::getDateCreated).reversed())
                .collect(Collectors.toList())
                .subList(0, limit);
    }

    @Transactional
    @Override
    public UserView registerUser(UserRegisterDTO user, String requestUrl) throws UserAlreadyExistsException, NullPointerException{
        /* Check for duplicate emails and username */
        if(emailExist(user.getEmail()) || usernameExist(user.getUserName())){
            throw new UserAlreadyExistsException("An account with this username/email already exists", new Exception("Custom Authentication Exception"));
        }

        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUserName())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .about(user.getAbout())
                .roles(Set.of(Role.USER))
                .build();

        File avatar;
        /* Decode User's Avatar from base64 format */
        //logger.info("User's image is: {}", user.getAvatar());
        if (user.getAvatar()!=null) {
            byte[] fileBytes = Base64Utils.decodeFromString(user.getAvatar().getBase64());

            avatar = File.builder()
                    .name(user.getAvatar().getName())
                    .contentType(user.getAvatar().getContentType())
                    .data(fileBytes)
                    .size(user.getAvatar().getSize())
                    .build();

            avatar.setUserAvatar(newUser);
            newUser.setAvatar(avatar);
        }
        if (user.getTopics()!=null){
            Set<Topic> chosenTopics = new HashSet<>();
            user.getTopics().forEach(topic -> {
                chosenTopics.add(topicService.findByTitle(topic.getTitle()));
            });
            logger.info("Topics: {}", chosenTopics);
            newUser.setTopics(chosenTopics);
            topicService.updateUsers(chosenTopics, newUser);
        }

        userRepository.save(newUser);

        String accessToken = authenticationService.generateAccessToken(newUser, requestUrl);
        String refreshToken = authenticationService.generateRefreshToken(newUser, requestUrl);


        UserView authenticatedUser = userRepository.findByUsernameAuth(newUser.getUsername());
        authenticatedUser.setAccessToken(accessToken);
        authenticatedUser.setRefreshToken(refreshToken);
        authenticatedUser.setAccessTokenExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000).getTime());
        authenticatedUser.setRefreshTokenExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000).getTime());

        logger.info("New User has registered: {} {} {} {}", user.getFirstName(), user.getEmail(), user.getTopics(), user.getAbout());
        return authenticatedUser;
    }

    /*@Override
    public List<Post> findFriendsPostsPageable(String username, Integer limit) {
        User user = userRepository.findByUsername(username);
        return postService.findFriendsPosts(user, limit);
    }*/

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

    private boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
