package com.core.rest.eden.base;

import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.services.UserTopicScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("user-scores-binders")
public class TopicScoreServiceBinders extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;
    private final UserTopicScoreService userTopicScoreService;

    @Override
    public void run(String... args) throws Exception {

        List<User> users = userService.findAll();
        users.forEach(userTopicScoreService::calculateUserScores);
    }
}
