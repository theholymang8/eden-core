package com.core.rest.eden.base;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.services.UserTopicScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("topic-score-testing")
public class TopicScoreServiceTesting extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;
    private final UserTopicScoreService userTopicScoreService;
    private final TopicService topicService;

    @Override
    public void run(String... args) throws Exception {


        User user = userService.findUserProfile("wdonisib");
        //Topic topic = topicService.findByTitle("Activism");

        //logger.info("{}",user);

        userTopicScoreService.calculateUserScores(user);
    }
}
