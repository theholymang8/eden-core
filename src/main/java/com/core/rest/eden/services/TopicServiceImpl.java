package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.TopicRepository;
import com.core.rest.eden.transfer.projections.FriendInterestsProjection;
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
            if (user != null){
                foundTopics.addAll(topicRepository.findAllByUsers(user));
            }
        }
        return foundTopics;
    }

    @Override
    public void updateUsers(Set<Topic> topics, User user) {
        topics.forEach(topic -> {
            if(topic.getUsers() == null){
                topic.setUsers(Set.of(user));
            }else{
                topic.getUsers().add(user);
            }
        });

    }

    @Override
    public List<FriendInterestsProjection> findFriendsInterests(Long userId) {
        return topicRepository.findFriendsInterests(userId);
    }
}
