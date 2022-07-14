package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.exceptions.UserAlreadyExistsException;
import com.core.rest.eden.repositories.UserRepository;
import com.core.rest.eden.transfer.DTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final PostService postService;
    private final AuthenticationService authenticationService;
    private final FileService fileService;
    private final FriendshipService friendshipService;
    private final GetTopicPostsService getTopicPostsService;
    private final PostClassifierService postClassifierService;
    private final SentimentAnalyzerService sentimentAnalyzerService;
    private final CerebrumRestService cerebrumRestService;
    private final UserTopicScoreService userTopicScoreService;

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    private final PasswordEncoder passwordEncoder;

    @Override
    public JpaRepository<User, Long> getRepository() {
        return userRepository;
    }

    @Override
    public List<User> findByTopics(Set<Topic> topics) {
        return userRepository.findAllByTopicsIn(topics);
    }

    @Override
    public List<Post> getRelatedPosts(String username, Integer limit, Integer page) {
        User user = userRepository.findByUsername(username);

        Set<Topic> userTopics = topicService.findByUsers(List.of(user));
        PostTopicsDTO preferredTopics = getTopicPostsService.getUserPreferencedTopics(userTopics);

        List<Post> relatedPosts = new ArrayList<>();
        preferredTopics.getUser_topics().forEach(topic-> {
            relatedPosts.addAll(postService.findByClusteredTopic(topic, limit, page));
        });


        List<Post> friendPosts = postService.findFriendsPosts(user, limit/2, page);

        List<Post> mixedPosts = new ArrayList<>(friendPosts);
        mixedPosts.addAll(relatedPosts);

        //Collections.shuffle(mixedPosts);

        friendPosts.forEach(post -> {
            logger.info("Friends's Posts : {}", post.getUser());
        });



        return mixedPosts;
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
    public List<Post> findPosts(String firstName, String lastName, Integer limit, Integer page) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        return postService.findUserPosts(user, limit, page);
    }

    @Override
    public List<Post> findPostsByUsername(String username, Integer limit, Integer page) {
        User user = userRepository.findByUsername(username);
        return postService.findUserPosts(user, limit, page);
    }

    @Override
    public void addRoleToUser(String firstName, String lastName, Role role) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        user.getRoles().add(role);
    }

    @Override
    public List<Post> findTopicRelatedPosts(List<String> usernames, Integer limit, Integer page) {
        List<User> users = new ArrayList<>();
        usernames.forEach(username -> users.add(userRepository.findByUsername(username)));
        Set<Topic> userRelatedTopics = topicService.findByUsers(users);

        return postService.findByTopics(userRelatedTopics, limit, page);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findUserProfile(String username) {
        return userRepository.findUserProfile(username);
    }

    @Override
    public Post uploadPost(PostDTO entity) throws ExecutionException, InterruptedException {
        User user = userRepository.findByUsername(entity.getUsername());
        Post newPost = Post.builder()
                .dateCreated(entity.getDateCreated())
                .body(entity.getBody())
                .likes(0)
                .user(user)
                .build();

        logger.info("User's interestes: {}", user.getTopics());
        /*newPost = postClassifierService.classifyPost(newPost);
        //Update user's interests
        updateUserInterests(user, newPost.getTopics());
        Sentiment postSentiment = sentimentAnalyzerService.analyzePost(newPost);
        newPost.setPostSentiment(postSentiment);*/
        newPost = classifyPost(user, newPost);

        if(entity.getImage()!=null){
            byte[] fileBytes = Base64Utils.decodeFromString(entity.getImage().getBase64());

            File fileEntity = File.builder()
                    .name(StringUtils.cleanPath(entity.getImage().getName()))
                    .contentType(entity.getImage().getContentType())
                    .post(newPost)
                    .user(user)
                    .data(fileBytes)
                    .build();
            newPost.setImage(fileEntity);
        }



        /*Thread classify_task = new Thread(() -> {
            this.classifyPost(user, newPost);
        });*/
        newPost = clusterPost(newPost);
        //Post finalNewPost = newPost;
        /*Thread cluster_task = new Thread(() -> {
            try {
                Thread.sleep(400);
                this.clusterPost(finalNewPost.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });*/
        //classify_task.start();
        //cluster_task.start();
        //newPost = future.get();
        postService.create(newPost);
        return newPost;
    }

    public Post classifyPost(User user, Post post) {
        post = postClassifierService.classifyPost(post);
        //Update user's interests
        updateUserInterests(user, post.getTopics());
        Sentiment postSentiment = sentimentAnalyzerService.analyzePost(post);
        post.setPostSentiment(postSentiment);
        //postService.update(post);
        Set<UserTopicScore> userTopicScores = userTopicScoreService.calculateUserScores(user);
        return post;
    }

    /*public void analyzePost(Post post) throws ExecutionException, InterruptedException {
        Sentiment postSentiment = sentimentAnalyzerService.analyzePost(post);
        post.setPostSentiment(postSentiment);
        postService.update(post);
    }*/

    public Post clusterPost(Post post) {
        //Future<Post> future = postClassifierService.clusterPost(post);
        post = postClassifierService.clusterPost(post);

        //post = future.get();

        logger.info("Post: {}", post);
        return post;
        //postService.update(post);

    }

    @Override
    public Post addComment(Post post, String username) {
        /*User user = userRepository.findByUsername(username);
        post*/
        return null;
    }

    @Override
    public User loadUserByEmail(String username) {
        return null;
    }

    @Override
    public List<User> findFriends(Long userId) {
        User user = userRepository.getById(userId);
        List<User> friendsAddressee = userRepository.findFriends(user);
        List<User> friendsRequester = userRepository.findFriendsRequester(user);

        List<User> userFriends = new ArrayList<>();

        userFriends.addAll(friendsAddressee);
        userFriends.addAll(friendsRequester);

        return userFriends;

    }

    @Override
    public List<Post> findFriendsPosts(String username, Integer limit) {
        /*List<User> friends = this.findFriends(this.findByUsername(username));
        List<Post> friendsPosts = new ArrayList<>();
        friends.forEach(friend -> friendsPosts.addAll(friend.getPosts()));

        limit = (friendsPosts.size() >= limit) ? limit : friendsPosts.size();

        return friendsPosts.stream()
                .sorted(Comparator.comparing(Post::getDateCreated).reversed())
                .collect(Collectors.toList())
                .subList(0, limit);*/
        return null;
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
            //logger.info("Topics: {}", chosenTopics);
            newUser.setTopics(chosenTopics);
            topicService.updateUsers(chosenTopics, newUser);
        }




        userRepository.save(newUser);
        Set<UserTopicScore> userTopicScores = userTopicScoreService.calculateUserScores(newUser);
        logger.info("User topic scores: {}", userTopicScores);
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

    @Override
    public void newFriendsRequest(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        friendshipService.requestFriendship(requester, addressee);
    }

    @Override
    public void acceptFriendRequest(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        friendshipService.acceptFriendship(requester, addressee);
    }

    @Override
    public void rejectFriendRequest(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        friendshipService.rejectFriendship(requester, addressee);
    }

    @Override
    public void deleteFriendship(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        friendshipService.deleteFriendship(requester, addressee);
    }

    @Override
    public List<Post> findFriendsPosts(String username, Integer limit, Integer page) {
        User user = userRepository.findByUsername(username);
        return postService.findFriendsPosts(user, limit, page);
    }

    @Override
    public Friendship isAlreadyFriends(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        return friendshipService.isAlreadyFriends(requester, addressee);
    }

    @Override
    public Boolean hasSentFriendRequest(Long requesterId, Long addresseeId) {
        User requester = userRepository.getById(requesterId);
        User addressee = userRepository.getById(addresseeId);
        return friendshipService.hasSentRequest(requester, addressee);
    }

    @Override
    public List<User> getAllFriendRequests(Long addresseeId) {
        User addressee = userRepository.getById(addresseeId);
        return friendshipService.getAllFriendRequests(addressee);
    }

    @Override
    public void updateSettings(UpdateSettingsDTO newSettings) {

        logger.info("DTO: {}", newSettings);

        User user = userRepository.findByUsername(newSettings.getUsername());

        if (newSettings.getNewUsername() != null) {
            logger.info("Username will change");
            user.setUsername(newSettings.getNewUsername());
        }

        if (newSettings.getNewEmail() != null) {
            user.setEmail(newSettings.getNewEmail());
        }

        if (newSettings.getNewTopics() != null) {
            Set<Topic> newTopics = new HashSet<>();
            newSettings.getNewTopics().forEach(topic -> {
                newTopics.add(topicService.findByTitle(topic));
            });
            user.setTopics(newTopics);
            logger.info("Topics: {}", newTopics);
        }

        logger.info("User's info after update: {} {} {}", user.getUsername(), user.getEmail(), user.getTopics());

        userRepository.save(user);
    }

    @Override
    public List<User> getRecommenderFriends(Long userId) {

        RecommendedFriendsDTO userIds = cerebrumRestService.getRecommendedFriends(userId);

        List<User> recommendedFriends = new ArrayList<>();

        userIds.getUserIds().forEach(id -> {
            if(friendshipService.isAlreadyFriends(userRepository.getById(userId), userRepository.getById(id))==null){
                recommendedFriends.add(userRepository.getById(id));
            }
        });

        return  recommendedFriends;
    }


    public void updateUserInterests(User user, Set<Topic> topics) {
        Set<Topic> userTopics = user.getTopics();
        logger.info("Topics: {}", userTopics);
        topics.forEach(topic -> {
            if(!(userTopics.contains(topic))) {
                userTopics.add(topic);
            }
        });
        userRepository.save(user);
        //topicService.updateUsers(userTopics, user);
    }

    /*@Override
    public List<FriendInterestsProjection> findFriendsInterest(Long userId) {
        return topicService.findFriendsInterests(userId);
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

    private static ArrayList<Post> sortHashMap(Map<Post,Integer> hashmap) {
        HashMap<Post,Integer> temp = hashmap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        temp.forEach((key, value) -> {
            log.info("Reverse Sorted Hashmap : {}", value);
        });
        ArrayList<Post> sortedValues = new ArrayList<>();
        temp.forEach((key, value) -> {
            sortedValues.add(key);
        });

        return sortedValues;
    }
}
