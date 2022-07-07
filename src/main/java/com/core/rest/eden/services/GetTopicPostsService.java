package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.transfer.DTO.PostTopicsDTO;

import java.util.List;
import java.util.Set;

public interface GetTopicPostsService {

    PostTopicsDTO getUserPreferencedTopics(Set<Topic> userTopics);

}
