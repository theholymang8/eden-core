package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.exceptions.NewsApiConcurrencyException;
import com.core.rest.eden.exceptions.UserAlreadyExistsException;
import com.core.rest.eden.repositories.UserRepository;
import com.core.rest.eden.transfer.DTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
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
    private final FriendshipService friendshipService;
    private final GetTopicPostsService getTopicPostsService;
    private final PostClassifierService postClassifierService;
    private final SentimentAnalyzerService sentimentAnalyzerService;
    private final CerebrumRestService cerebrumRestService;
    private final UserTopicScoreService userTopicScoreService;
    private final NewsRecommendationService newsRecommendationService;

    private ExecutorService executor = Executors.newFixedThreadPool(1);

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
    @Cacheable("relatedPosts")
    public List<Post> getRelatedPosts(String username, Integer limit, Integer page) {
        User user = userRepository.findByUsername(username);

        Set<Topic> userTopics = topicService.findByUsers(List.of(user));
        PostTopicsDTO preferredTopics = getTopicPostsService.getUserPreferencedTopics(userTopics);

        List<Post> relatedPosts = new ArrayList<>();

        //If user hasn't registered any interests
        if (!(preferredTopics.getUser_topics().size() > 0)) {
            List<Integer> clusters = postService.findClusters();
            clusters.forEach(cluster -> {
                relatedPosts.addAll(postService.findByClusteredTopic(cluster, limit, page));
            });
        }


        preferredTopics.getUser_topics().forEach(topic-> {
            relatedPosts.addAll(postService.findByClusteredTopic(topic, limit, page));
        });


        List<Post> friendPosts = postService.findFriendsPosts(user, limit/2, page);

        List<Post> mixedPosts = new ArrayList<>(friendPosts);
        mixedPosts.addAll(relatedPosts);

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
    @Cacheable("topicRelatedPosts")
    public List<Post> findTopicRelatedPosts(List<String> usernames, Integer limit, Integer page) {
        List<User> users = new ArrayList<>();
        usernames.forEach(username -> users.add(userRepository.findByUsername(username)));
        Set<Topic> userRelatedTopics = topicService.findByUsers(users);
        Random rand = new Random();

        //Generate Random Topic for users who have not registered interests
        if (!(userRelatedTopics.size() > 0)) {
            List<Topic> allTopics = topicService.findAll();
            int randInt = rand.nextInt(userRelatedTopics.size()-1);
            while (userRelatedTopics.size() < 4) {
                if (!(userRelatedTopics.contains(allTopics.get(randInt)))) {
                    userRelatedTopics.add(allTopics.get(randInt));
                }
            }
        }
        return postService.findByTopics(userRelatedTopics, users.get(0), limit, page);

        //TO-DO sort by likes
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
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
    public Post uploadPost(PostDTO entity) throws ExecutionException, InterruptedException {
        Future<Map<String, ? extends BaseModel>> future = createPost(entity);

        Map<String, ? extends BaseModel> entities = future.get();
        User user = (User) entities.get("user");
        Post newPost = (Post) entities.get("post");

        Thread classify_task = new Thread(() -> {
            try {
                classifyPost(user.getUsername(), newPost.getId());
            } catch (InterruptedException e) {
                logger.error("Classifier Thread interrupted");
            }
        });


        classify_task.start();

        return newPost;
    }

    @Async
    public Future<Map<String,? extends BaseModel>> createPost(PostDTO entity) {
        User user = userRepository.findByUsername(entity.getUsername());
        Post newPost = Post.builder()
                .dateCreated(entity.getDateCreated())
                .body(entity.getBody())
                .likes(0)
                .user(user)
                .build();
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
        postService.create(newPost);
        Map<String, ? extends BaseModel> entities = Map.of(
                "user", user,
                "post", newPost
        );
        return new AsyncResult<>(entities);
    }

    public void classifyPost(String username, Long postId) throws InterruptedException {
        Post post = null;
        while (post == null) {
            try {
                post = postService.find(postId);

            }catch(NoSuchElementException e){
                Thread.sleep(1000);
                logger.warn("Object Not Persisted yet!");
            }
        }
        User user = userRepository.findUserTopicsAndScore(username);

        post = postClassifierService.classifyPost(post);

        //Update user's interests
        updateUserInterests(user, post.getTopics());
        Sentiment postSentiment = sentimentAnalyzerService.analyzePost(post);
        post.setPostSentiment(postSentiment);
        logger.info("Registered Post: {}", post);
        postService.update(post);

        Post finalPost = post;
        Thread cluster_task = new Thread(() -> {
            try {
                clusterPost(finalPost.getId());
            } catch (InterruptedException e) {
                logger.error("Cluster Thread interrupted");
            }

        });

        userTopicScoreService.calculateUserScores(user);
        cluster_task.start();
    }


    public void clusterPost(Long postId) throws InterruptedException {
        Post post= null;
        while (post == null) {
            try {
                post = postService.find(postId);

            }catch(NoSuchElementException e){
                Thread.sleep(1000);
                logger.warn("Post Not Persisted yet!");
            }

        }
        post = postClassifierService.clusterPost(post);

        postService.update(post);

    }

    @Override
    @Cacheable(value = "relatedNews")
    public List<NewsDTO> getRelatedNews(String username) throws NewsApiConcurrencyException{
        User user = userRepository.findUserTopicsAndScore(username);
        Set<Topic> userTopics = user.getTopics();
        List<NewsDTO> userRelatedNews = new ArrayList<>();
        userTopics.forEach(topic -> {
            Future<List<NewsDTO>> userRelatedResult = newsRecommendationService.getNews(topic.getTitle());
            try {
                List<NewsDTO> topicRelatedNews = userRelatedResult.get();
                topicRelatedNews.forEach(newsDTO -> newsDTO.setTopic(topic.getTitle()));
                //Filter body
                topicRelatedNews.forEach(newsDTO -> newsDTO.setAbstractNew(this.filterBody(newsDTO.getSnippet(), newsDTO.getLeadParagraph())));
                userRelatedNews.addAll(topicRelatedNews);
            } catch (InterruptedException | ExecutionException e) {
                throw new NewsApiConcurrencyException("Thread Interruption during news fetching", e);
            }
        });
        return userRelatedNews;
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
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
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
    @Cacheable("recommendedFriends")
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
        topics.forEach(topic -> {
            if(!(userTopics.contains(topic))) {
                userTopics.add(topic);
            }
        });
        userRepository.save(user);
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

    //Filter article body
    private String filterBody(String leadParagraph, String snippet) {
        if (leadParagraph.equals(snippet))
            return leadParagraph;
        return leadParagraph.concat(snippet);
    }

    private boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

}
