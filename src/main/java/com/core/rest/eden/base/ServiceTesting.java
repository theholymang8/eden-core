package com.core.rest.eden.base;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.CommentService;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.CommentView;
import com.core.rest.eden.transfer.DTO.UserPostView;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Profile(value = "service-testing")
public class ServiceTesting extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    private final PostService postService;

    private final CommentService commentService;



    @Override
    public void run(String... args) {

        logger.info("Testing user's posts");

        /*userService.findAll().forEach(user ->
                user.getPosts().forEach(post ->
                        logger.info("This user has these posts: {}", post)));*/

        //userService.findByName("Ioannis", "Anastasopoulos")
        //        .getPosts()
        //        .forEach(post -> logger.info("Ioannis has this post: {}", post));

        logger.info("User: {}", userService.findByName("Ioannis", "Anastasopoulos"));

        User user = userService.findByName("Ioannis", "Anastasopoulos");

        List<Post> posts = userService.findPosts("Ioannis", "Anastasopoulos", 5);

        List<CommentView> postComments = commentService.postComments(posts.get(0));
        //List<Comment> comments = commentService.userComments(posts.get(0));

        logger.info("Post's comments are : {}", commentService.postComments(posts.get(0)));

        String name = "Ioannis" + "Anastasopoulos";

        //UserPostView userPostView = new UserPostView(name, posts.get(0).getDateCreated(), posts.get(0).getDateUpdated(), posts.get(0).getBody(), posts.get(0).getLikes(), postComments);

        //logger.info("Testing userPostView : {}", userPostView);
    }
}
