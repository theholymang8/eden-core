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
        Long usersPostsPerTopic = postService.countUserPostsByTopic(user, topic);
        Long usersPositivePostsPerTopic = postService.countUserPostsBySentimentAndTopic(user, topic, Sentiment.POSITIVE);
        Long usersNeutralPostsPerTopic = postService.countUserPostsBySentimentAndTopic(user, topic, Sentiment.NEUTRAL);


        float sentimentScore = usersPostsPerTopic != 0 ? ((float)usersPositivePostsPerTopic+usersNeutralPostsPerTopic)/usersPostsPerTopic : 1;

        //Add sentiment bias to the user topic score
        float userTopicScore = 1 * sentimentScore;

        logger.info("User's topic score is: {}", userTopicScore);
        return userTopicScore;
    }

    @Override
    public Set<UserTopicScore> calculateUserScores(User user) {

        List<Topic> allTopics = topicService.findAll();

        Map<Topic, Float> topicScoreMap = new HashMap<Topic, Float>();
        allTopics.forEach(topic -> {
            topicScoreMap.put(topic, 0.5F);
        });

        User userProxy = User.builder()
                .id(user.getId())
                .build();

        Set<Topic> usersTopics = topicService.findByUsers(List.of(user));

        usersTopics.forEach(topic -> {
            float topicScore = this.calculateUserTopicScore(user, topic);
            topicScoreMap.put(topic, topicScore);
        });


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


        if (user.getUserTopicScores() != null){
            user.getUserTopicScores().clear();
            user.getUserTopicScores().addAll(userTopicMatrix);
            return user.getUserTopicScores();
        }


        user.setUserTopicScores(userTopicMatrix);

        return user.getUserTopicScores();
    }
}
