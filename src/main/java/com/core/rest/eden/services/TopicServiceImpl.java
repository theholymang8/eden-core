package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl extends BaseServiceImpl<Topic> implements TopicService{

    private final TopicRepository topicRepository;

    private final UserService userService;

    @Override
    public JpaRepository<Topic, Long> getRepository() {
        return topicRepository;
    }

    @Override
    public Topic findByTitle(String title) {
        return topicRepository.findByTitle(title);
    }

    @Override
    public List<Topic> findByUsers(List<String> users) {
        List<Topic> foundTopics = new ArrayList<>();
        for(final String user : users){
            /* Split user name into first Name and last Name*/
            String[] nameSplit = user.split("\\s+");
            User foundUser = userService.findByName(nameSplit[0], nameSplit[1]);
            if (foundUser != null){
                logger.trace("Found Topics for User : {} ", foundUser);
                foundTopics.addAll(topicRepository.findAllByUsers(foundUser));
            }
        }
        return foundTopics;
    }
}
