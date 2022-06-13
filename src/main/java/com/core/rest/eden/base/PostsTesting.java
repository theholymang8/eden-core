package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Profile("post-testing")
public class PostsTesting extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;

    @Override
    public void run(String... args) throws Exception {
        //postService.findAll().forEach(post -> logger.info("Post: {}", post.getTopics()));

        List<Post> posts = postService.findAll();

        Long postID = posts.get(3).getId();

        logger.info("Post id: {}", postID);

        //postService.find(postID).getTopics().forEach(topic -> logger.info("Post has this topic related to it: {}", topic.getTitle()));

    }
}
