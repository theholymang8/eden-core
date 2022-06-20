package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("post-testing")
public class PostTesting extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;

    @Override
    public void run(String... args) throws Exception {
        Post post = postService.find(65L);
        logger.info("Post: {}", post);
    }
}