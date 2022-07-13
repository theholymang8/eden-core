package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Sentiment;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.domain.UserTopicScore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserTopicScoreServiceImpl extends AbstractLogComponent implements UserTopicScoreService {

    private final PostService postService;
    private final TopicService topicService;

    @Override
    public float calculateUserTopicScore(User user, Topic topic) {
        //Long usersTotalPosts = postService.countPostsByUser(user);
        Long usersPostsPerTopic = postService.countUserPostsByTopic(user, topic);
        Long usersPositivePostsPerTopic = postService.countUserPostsBySentimentAndTopic(user, topic, Sentiment.POSITIVE);
        Long usersNeutralPostsPerTopic = postService.countUserPostsBySentimentAndTopic(user, topic, Sentiment.NEUTRAL);
        //Long usersNegativePostsPerTopic = postService.countUserPostsBySentimentAndTopic(user, topic, Sentiment.NEGATIVE);

        //float topicScore = (float)usersPostsPerTopic/usersTotalPosts;

        float sentimentScore = usersPostsPerTopic != 0 ? ((float)usersPositivePostsPerTopic+usersNeutralPostsPerTopic)/usersPostsPerTopic : 1;

        //Add sentiment bias to the user topic score
        float userTopicScore = 1 * sentimentScore;

        //logger.info("User's total posts are: {}", usersTotalPosts);
        logger.info("User's posts for {} are: {}",topic.getTitle(), usersPostsPerTopic);
        logger.info("User's positive posts for {} are: {}",topic.getTitle(), usersPositivePostsPerTopic);
        logger.info("User's neutral posts for {} are: {}",topic.getTitle(), usersNeutralPostsPerTopic);
        //logger.info("User's negative posts for {} are: {}",topic.getTitle(), usersNegativePostsPerTopic);
        logger.info("User's topic score is: {}", userTopicScore);
        return userTopicScore;
        //logger.info("User's sentiment score is: {}", userTopicScore);

    }

    @Override
    public Set<UserTopicScore> calculateUserScores(User user) {

        List<Topic> allTopics = topicService.findAll();

        Map<Topic, Float> topicScoreMap = new HashMap<Topic, Float>();
        allTopics.forEach(topic -> {
            topicScoreMap.put(topic, 0F);
        });

        User userProxy = User.builder()
                .id(user.getId())
                .build();

        Set<Topic> usersTopics = topicService.findByUsers(List.of(user));

        usersTopics.forEach(topic -> {
            float topicScore = this.calculateUserTopicScore(user, topic);
            topicScoreMap.put(topic, topicScore);
        });

        logger.info("User's topics: {}", usersTopics);

        //logger.info("User: {}", user);

        Set<UserTopicScore> userTopicMatrix = new HashSet<>();
        allTopics.forEach(topic -> {
            userTopicMatrix.add(
                    UserTopicScore.builder()
                            .user(userProxy)
                            .topic(topic)
                            .topicScore(topicScoreMap.get(topic))
                            .build()
            );
        });

        user.setUserTopicScores(userTopicMatrix);
        /*userService.update(user);*/

        logger.info("User's Interest Scores: {}", userTopicMatrix);

        return user.getUserTopicScores();
    }
}
