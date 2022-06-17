package com.core.rest.eden.services;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.repositories.PostRepository;
import com.core.rest.eden.transfer.DTO.CommentView;
import com.core.rest.eden.transfer.DTO.PostDTO;
import com.core.rest.eden.transfer.DTO.UserPostView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService{

    private final PostRepository postRepository;

    private final CommentService commentService;

    private final FileService fileService;

    @Override
    public JpaRepository<Post, Long> getRepository() {
        return postRepository;
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findByIdCustom(id);
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
    public List<Post> findByTopics(Set<Topic> topics, Integer limit, Integer page) {
        //List<Post> relatedPosts = postRepository.findDistinctAllByTopicsIn(topics, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "dateCreated")));

        //relatedPosts.forEach(post -> logger.info("Found post: {} with these topics: {}", post.getId(), post.getTopics()));

        //logger.info("Getting Posts: {}", postRepository.findAllByTopicsIn(topics, PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "dateCreated"))));
        return postRepository.findDistinctAllByTopicsIn(topics, PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "dateCreated")));
    }

    /*@Override
    public List<Post> findFriendsPosts(User user, Integer limit) {
        return postRepository.findFriendsPosts(user, PageRequest.of(0, limit));
    }*/

    @Override
    public Post addLike(Long id) throws NoSuchElementException{
        Post foundPost = postRepository.getById(id);
        //Post foundPost = postRepository.findById(id).orElseThrow(NoSuchElementException::new);
        foundPost.setLikes(foundPost.getLikes()+1);
        return postRepository.save(foundPost);
    }

    @Override
    public Post addComment(Long postID, Comment comment) {
        Post post = postRepository.getById(postID);
        comment.setPost(post);
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
    public Post deleteComment(Long postID, Comment comment) {
        Post post = postRepository.getById(postID);
        commentService.delete(comment);
        logger.info("Comments: {}", post.getComments());
        post.getComments().remove(comment);
        return postRepository.save(post);
    }

   /* @Override
    public Post uploadPost(PostDTO entity) {
        Post newPost = Post.builder()
                .dateCreated(entity.getDateCreated())
                .body(entity.getBody())
                .likes(0)
                //.user(entity.getUser())
                .build();

        if(entity.getImage()!=null){
            byte[] fileBytes = Base64Utils.decodeFromString(entity.getImage().getBase64());

            File fileEntity = File.builder()
                    .name(StringUtils.cleanPath(entity.getImage().getName()))
                    .contentType(entity.getImage().getContentType())
                    .data(fileBytes)
                    .build();
            newPost.setImage(fileEntity);
            *//*fileEntity.setPost(newPost);
            fileService.create(fileEntity);*//*
        }

        logger.info("Post: {}", newPost);

        postRepository.save(newPost);
        return newPost;

    }*/

    @Override
    public void addLikev2(Post post) {
        post.setLikes(post.getLikes()+1);
        post.setDateUpdated(LocalDateTime.now());
        logger.info("Post by service is :{}", post);
        // logger.info("post has: {} likes", post.getLikes());
        postRepository.save(post);
    }


}
