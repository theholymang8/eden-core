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

import java.util.*;

@RequiredArgsConstructor
@Component
@Profile("topic-binders")
public class TopicBinders  extends AbstractLogComponent implements CommandLineRunner{

    public static <T> List<T> removeDuplicates(List<T> list)
    {

        // Create a new ArrayList
        List<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    private final UserService userService;
    private final TopicService topicService;
    private final PostService postService;

    @Override
    public void run(String... args) {



        List<User> users = userService.findAll();
        List<Post>  posts = postService.findAll();
        List<Topic> topics = topicService.findAll();


        /*users.get(0).setTopics(Set.of(topics.get(1), topics.get(2), topics.get(3)));
        topicService.updateUsers(users.get(0).getTopics(), users.get(0));
        userService.update(users.get(0));*/
        /*users.forEach(user -> {
            Collections.shuffle(topics);
            Integer randomTopicsSeriesLength = 5;
            List<Topic> randomTopics = topics.subList(0, randomTopicsSeriesLength);
            List<Topic> removedDuplicated = removeDuplicates(randomTopics);
            Set<Topic> setOfTopics = new HashSet<>(removedDuplicated);
            topicService.updateUsers(setOfTopics, user);
            *//*setOfTopics.forEach(topic -> {
                logger.info("User {} has this topic: {}", user.getUsername(), topic.getTitle());
            });*//*
            //logger.info("Topics remaining: {}", setOfTopics);
            user.setTopics(setOfTopics);
            logger.info("User: {}", user);
            //userService.update(user);
        });

        List<User> soleUsers = removeDuplicates(users);

        users.forEach(user -> {
            logger.info("User: {} has these topics : {}", user.getUsername(), user.getTopics());
        });
        //userService.updateAll(users);
        userService.updateAll(soleUsers);*/

        topics.forEach(topic -> {
            Collections.shuffle(posts);
            //topic.setUsers(Set.of(users.get(0), users.get(1), users.get(2), users.get(3)));
            //topic.setUsers();
            topic.setPosts(Set.of(posts.get(0)));
            logger.info("Topic: {}", topic);
            topicService.update(topic);
        });


        //logger.info("Binded topics to {} users.", users.size());

        logger.info("Binded topics to {} posts.", posts.size());
    }
}
