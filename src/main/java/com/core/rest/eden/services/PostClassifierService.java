package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;

import java.util.concurrent.Future;

public interface PostClassifierService {

    Post classifyPost(Post post);

    Post clusterPost(Post post);

}
