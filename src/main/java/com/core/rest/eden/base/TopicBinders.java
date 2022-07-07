package com.core.rest.eden.base;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
@Profile("topic-binders")
public class TopicBinders  extends AbstractLogComponent implements CommandLineRunner{


    private final UserService userService;
    private final TopicService topicService;

    @Override
    public void run(String... args) {



        List<User> users = userService.findAll();
        List<Topic> topics = topicService.findAll();


        Random rn = new Random();
        Integer max = topics.size() - 1;
        Integer min = 0;

        users.forEach(user -> {
            Integer topicCount = 0;
            Set<Topic> userTopics = new HashSet<>();
            while(topicCount<3){
                Integer index = rn.nextInt(max-min)+1;
                if(!userTopics.contains(topics.get(index))) {
                    userTopics.add(topics.get(index));
                    topicCount+=1;
                }
            }
            user.setTopics(userTopics);
            userService.update(user);
        });

        logger.info("Bound random topics to all users.");

    }
}
