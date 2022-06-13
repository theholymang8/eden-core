package com.core.rest.eden.base;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.CommentService;
import com.core.rest.eden.services.FileService;
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

    private final FileService fileService;


    @Override
    public void run(String... args) {

        logger.info("Testing user's posts");

        /*userService.findAll().forEach(user ->
                user.getPosts().forEach(post ->
                        logger.info("This user has these posts: {}", post)));*/

        //userService.findByName("Ioannis", "Anastasopoulos")
        //        .getPosts()
        //        .forEach(post -> logger.info("Ioannis has this post: {}", post));

        //List<User> users = userService.findAll();

        //users.forEach(user -> logger.info("User: {}, {}", user.getFirstName(), user.getLastName()));

        //User user = userService.findByUsername("vanas");

        //User user = userService.findByName("Pavlos", "Poulos");

        //List<Post> posts = userService.findPosts("Ioannis", "Anastasopoulos", 5);

        //List<CommentView> postComments = commentService.postComments(posts.get(0));
        //List<Comment> comments = commentService.userComments(posts.get(0));

        //logger.info("Post's comments are : {}", commentService.postComments(posts.get(0)));

        //String name = "Ioannis" + "Anastasopoulos";

        //user.getTopics().forEach(topic -> logger.info("Topic: {}", topic.getTitle()));

        //UserPostView userPostView = new UserPostView(name, posts.get(0).getDateCreated(), posts.get(0).getDateUpdated(), posts.get(0).getBody(), posts.get(0).getLikes(), postComments);

        //logger.info("Testing userPostView : {}", userPostView);

        //user.getTopics().forEach(topic -> logger.info("Vasilios has these topics: {}", topic.getTitle()));

        //userService.findFriends(user).forEach(friend -> logger.info("Giannis has the following friend: {}", friend));

        logger.info("Found User {}", userService.findByUsername("xristinpapadim").getAvatar());

        //userService.findByUsername("ganast").getFiles().forEach(file -> logger.info("Found one file: {}", file));

        //logger.info("Found file {}", fileService.findByName("cv_pic.jpg").getUser());

    }
}
