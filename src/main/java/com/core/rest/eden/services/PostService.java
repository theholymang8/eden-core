package com.core.rest.eden.services;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.transfer.DTO.PostDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PostService extends BaseService<Post, Long>{

    @Query(value = "select p from Post p left join fetch p.topics left join fetch p.comments where p.id= :id")
    Post findById(Long id);

    List<Post> findByClusteredTopic(Integer clusteredTopic, Integer limit, Integer page);

    List<Post> findRecentPosts(Integer limit);

    List<Post> findUserPosts(User user, Integer limit, Integer page);

    List<Post> findByTopics(Set<Topic> topics, Integer limit, Integer page);

    Post addLike(Long id);

    Post addComment(Long postID, Comment comment);

    Post deleteComment(Long postID, Comment comment);

    List<Post> findFriendsPosts(User user, Integer limit, Integer page);

    /*Post uploadPost(PostDTO postDTO);*/

    void addLikev2(Post post);
}
