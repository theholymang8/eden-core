package com.core.rest.eden.base;

import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile(value = "service-testing")
public class ServiceTesting extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    private final PostService postService;

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
    }
}
