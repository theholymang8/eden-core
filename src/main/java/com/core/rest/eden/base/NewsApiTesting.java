package com.core.rest.eden.base;

import com.core.rest.eden.services.NewsRecommendationService;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.NewsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
@Profile("news-api-testing")
public class NewsApiTesting extends AbstractLogComponent implements CommandLineRunner {

    private final NewsRecommendationService newsRecommendationService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        String query = "Artificial Intelligence";

        //Future<List<NewsDTO>> future =  newsRecommendationService.getNews(query);

        //List<NewsDTO> news = future.get();

        List<NewsDTO> news = userService.getRelatedNews("ganast");



        logger.info("User's friends : {}", userService.getAllFriendRequests(411L));

        news.forEach(neww -> logger.info("Headline: {}", neww.getLeadParagraph()));
        logger.info("Done!");

    }
}
