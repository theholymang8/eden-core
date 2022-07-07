package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.GetTopicPostsService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.PostTopicsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Profile("recommender-tester")
@RequiredArgsConstructor
public class RecommenderServiceTesting extends AbstractLogComponent implements CommandLineRunner {

    private final GetTopicPostsService topicRecommenderService;
    private final UserService userService;
    private final TopicService topicService;

    @Override
    public void run(String... args) throws Exception {
        User user = userService.findByUsername("ganast");
        List<Post> relatedPosts = userService.getRelatedPosts("ganast", 10, 0);

        relatedPosts.forEach(post -> {
            logger.info("Post: {}", post);
        });
        //Set<Topic> userTopics = topicService.findByUsers(List.of(user));

        //Set<Topic> userTopics = user.getTopics();

        //PostTopicsDTO recommendedTopics = topicRecommenderService.getUserPreferencedTopics(userTopics);

        //logger.info("Recommended Topics: {}", recommendedTopics);
    }
}
