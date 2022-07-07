package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Sentiment;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.SentimentAnalyzerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("sentiment-analyzer")
public class SentimentClassifierServiceTesting extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;
    private final SentimentAnalyzerService sentimentAnalyzerService;

    public void analyzePostsSentiments(){
        List<Post> posts = postService.findAll();

        posts.forEach(post -> {
            Sentiment sentiment = sentimentAnalyzerService.analyzePost(post);
            post.setPostSentiment(sentiment);
            postService.update(post);
        });

    }

    @Override
    public void run(String... args) throws Exception {



        Post post = postService.find(150248L);
        Sentiment sentiment = sentimentAnalyzerService.analyzePost(post);
        logger.info("Sentiment of Post is: {}", sentiment);



    }
}
