package com.core.rest.eden.services;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.DTO.PostDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PostService extends BaseService<Post, Long>{

    Post findById(Long id);

    List<Post> findRecentPosts(Integer limit);

    List<Post> findUserPosts(User user, Integer limit, Integer page);

    List<Post> findByTopics(Set<Topic> topics, Integer limit, Integer page);

    Post addLike(Long id);

    /*Post uploadPost(PostDTO postDTO);*/

    void addLikev2(Post post);
}
