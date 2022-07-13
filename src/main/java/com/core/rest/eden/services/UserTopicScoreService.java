package com.core.rest.eden.services;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.domain.UserTopicScore;

import java.util.Set;

public interface UserTopicScoreService {

    float calculateUserTopicScore(User user, Topic topic);

    Set<UserTopicScore> calculateUserScores(User user);

}
