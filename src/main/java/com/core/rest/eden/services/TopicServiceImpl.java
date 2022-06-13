package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl extends BaseServiceImpl<Topic> implements TopicService{

    private final TopicRepository topicRepository;

    @Override
    public JpaRepository<Topic, Long> getRepository() {
        return topicRepository;
    }

    @Override
    public Topic findByTitle(String title) {
        return topicRepository.findByTitle(title);
    }

    @Override
    public Set<Topic> findByUsers(List<User> users) {
        Set<Topic> foundTopics = new HashSet<>();
        for(final User user : users){
            /* Split user name into first Name and last Name*/
            //String[] nameSplit = user.split("\\s+");
            //User foundUser = userService.findByName(nameSplit[0], nameSplit[1]);
            //User foundUser = userService.findByUsername(username);
            logger.info("User: {}", user);
            if (user != null){
                //logger.trace("Found Topics for User : {} ", user);
                foundTopics.addAll(topicRepository.findAllByUsers(user));
            }
        }
        return foundTopics;
    }

    @Override
    public void updateUsers(Set<Topic> topics, User user) {
        topics.forEach(topic -> {
            if(topic.getUsers() == null){
                //logger.info("Topics have no users");
                topic.setUsers(Set.of(user));
            }else{
                topic.getUsers().add(user);
                //logger.info("Topics have users");
            }
            //topicRepository.save(topic);
        });

    }
}
