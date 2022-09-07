package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Sentiment;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.domain.UserPostBehaviour;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostBehaviourServiceImpl extends AbstractLogComponent implements PostBehaviourService {

    private final UserService userService;
    private final PostService postService;

    @Override
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
    public Post addLike(Long postId, Long userId) {
        Post foundPost = postService.find(postId);
        User userPost = userService.find(userId);

        //Check if there is an interaction with the post already
        Set<UserPostBehaviour> usersInteractions = userPost.getUserBehavior();
        for (UserPostBehaviour usersInteraction : usersInteractions) {
            if (usersInteraction.getPost().equals(foundPost)){
                if (usersInteraction.getSentiment()== Sentiment.NEGATIVE){
                    usersInteraction.setSentiment(Sentiment.POSITIVE);
                    foundPost.setLikes(foundPost.getLikes()+1);
                    userService.update(userPost);
                    postService.update(foundPost);
                }
                return foundPost;
            }
        }

        UserPostBehaviour postBehaviour = UserPostBehaviour.builder()
                .post(foundPost)
                .user(userPost)
                .sentiment(Sentiment.POSITIVE)
                .build();

        userPost.getUserBehavior().add(postBehaviour);
        foundPost.setLikes(foundPost.getLikes()+1);

        userService.update(userPost);

        return foundPost;

    }

    @Override
    @CacheEvict(value = {"relatedPosts", "topicRelatedPosts", "recommendedFriends"} , allEntries = true)
    public Post addDislike(Long postId, Long userId) {
        Post foundPost = postService.find(postId);
        User userPost = userService.find(userId);

        Set<UserPostBehaviour> usersInteractions = userPost.getUserBehavior();
        for (UserPostBehaviour usersInteraction : usersInteractions) {
            if (usersInteraction.getPost().equals(foundPost)){
                if (usersInteraction.getSentiment() == Sentiment.POSITIVE){
                    usersInteraction.setSentiment(Sentiment.NEGATIVE);
                    if(foundPost.getLikes() > 0) {
                        foundPost.setLikes(foundPost.getLikes()-1);
                    }
                    userService.update(userPost);
                    postService.update(foundPost);
                }
                return foundPost;
            }
        }

        UserPostBehaviour postBehaviour = UserPostBehaviour.builder()
                .post(foundPost)
                .user(userPost)
                .sentiment(Sentiment.NEGATIVE)
                .build();

        userPost.getUserBehavior().add(postBehaviour);

        postService.update(foundPost);
        userService.update(userPost);

        return foundPost;

    }

}
