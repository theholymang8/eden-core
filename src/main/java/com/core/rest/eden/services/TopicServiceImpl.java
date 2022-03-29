package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.repositories.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl extends BaseServiceImpl<Topic> implements TopicService{

    private final TopicRepository topicRepository;

    @Override
    public JpaRepository<Topic, Long> getRepository() {
        return topicRepository;
    }
}
