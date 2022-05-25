package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Profile("topic-binders")
public class TopicBinders  extends AbstractLogComponent implements CommandLineRunner{

    private final UserService userService;
    private final TopicService topicService;
    private final PostService postService;

    @Override
    public void run(String... args) {

        List<User> users = userService.findAll();
        List<Post>  posts = postService.findAll();
        List<Topic> topics = topicService.findAll();

        topics.forEach(topic -> {
            Collections.shuffle(users);
            Collections.shuffle(posts);
            topic.setUsers(Set.of(users.get(0), users.get(1), users.get(2), users.get(3)));
            topic.setPosts(Set.of(posts.get(0)));
            logger.info("Topic: {}", topic);
            topicService.update(topic);
        });


        logger.info("Binded topics to {} users.", users.size());

        logger.info("Binded topics to {} posts.", posts.size());
    }
}
