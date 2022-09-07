package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService{

    private final PostRepository postRepository;

    private final CommentService commentService;

    private final SentimentAnalyzerService sentimentAnalyzerService;

    @Override
    public JpaRepository<Post, Long> getRepository() {
        return postRepository;
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findByIdCustom(id);
    }

    @Override
    public List<Post> findByClusteredTopic(Integer clusteredTopic, Integer limit, Integer page) {
        return postRepository.findPostsByClusteredTopic(clusteredTopic, PageRequest.of(page,limit));
    }

    @Override
    public List<Post> findRecentPosts(Integer limit) {
        return postRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "dateCreated"))).getContent();
    }

    @Override
    public List<Post> findUserPosts(User user, Integer limit, Integer page) {
       return postRepository.findAllByUser(user, PageRequest.of(page,limit));
    }

    @Override
    public Long countPostsByUser(User user) {
        return postRepository.countAllByUser(user);
    }

    @Override
    public Long countUserPostsByTopic(User user, Topic topic) {
        return postRepository.countAllByUserAndTopicsIn(user, Set.of(topic));
    }

    @Override
    public Long countUserPostsBySentimentAndTopic(User user, Topic topic, Sentiment sentiment) {
        return postRepository.countAllByUserAndTopicsInAndPostSentiment(user, Set.of(topic), sentiment);
    }

    @Override
    public List<Post> findByTopics(Set<Topic> topics, User user, Integer limit, Integer page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(
                new Sort.Order(Sort.Direction.DESC, "likes")
        );
        sorts.add(
                new Sort.Order(Sort.Direction.DESC, "dateCreated")
        );
        return postRepository.findDistinctAllByTopicsInAndUserNot(topics, user, PageRequest.of(page, limit, Sort.by(sorts)));
    }

    @Override
    public List<Post> findFriendsPosts(User user, Integer limit, Integer page) {
        List<Post> friendPostsRequester = postRepository.findFriendsPostsByRequester(user, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "dateCreated")));
        List<Post> friendPostsAddressee = postRepository.findFriendsPostsByAddressee(user, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "dateCreated")));
        List<Post> allFriendsPosts = new ArrayList<>();
        allFriendsPosts.addAll(friendPostsRequester);
        allFriendsPosts.addAll(friendPostsAddressee);
        return allFriendsPosts;
    }

    @Override
    public List<Integer> findClusters() {
        return postRepository.findClusters();
    }

    @Override
    public Post addLike(Long id) throws NoSuchElementException{
        Post foundPost = postRepository.getById(id);

        foundPost.setLikes(foundPost.getLikes()+1);
        return postRepository.save(foundPost);
    }

    @Override
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
    public Post addComment(Long postID, Comment comment) {
        Post post = postRepository.getById(postID);
        comment.setPost(post);
        Sentiment postSentiment = sentimentAnalyzerService.analyzeComment(comment);
        comment.setSentiment(postSentiment);
        logger.info("Post Comments: {}", post.getBody());
        //return post;
        if(post.getComments() == null) {
            post.setComments(Set.of(comment));
        }else{
            post.getComments().add(comment);
        }
        return postRepository.save(post);
    }

    @Override
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
    public Post deleteComment(Long postID, Comment comment) {
        Post post = postRepository.getById(postID);
        commentService.delete(comment);
        post.getComments().remove(comment);
        return postRepository.save(post);
    }


    @Override
    public void addLikev2(Post post) {
        post.setLikes(post.getLikes()+1);
        post.setDateUpdated(LocalDateTime.now());
        postRepository.save(post);
    }


}
