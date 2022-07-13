package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;

public interface PostBehaviourService {

    Post addLike(Long postId, Long userId);

    Post addDislike(Long postId, Long userId);

}
