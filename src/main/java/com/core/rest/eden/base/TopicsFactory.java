package com.core.rest.eden.base;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("generate-topics")
public class TopicsFactory extends AbstractLogComponent implements CommandLineRunner {

    private final TopicService topicService;

    private final UserService userService;


    @Override
    public void run(String... args){

        List<Topic> topics = List.of(
          Topic.builder()
                  .title("Artificial Intelligence")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Cryptocurrencies")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("General Technology News")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Space & Science")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Technology Products")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Activism")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Economy")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Environment")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Health & Politics")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("International & Foreign Politics")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Miscelaneous Politics & Legislation")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Finance")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Basketball & NBA")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Football")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("General & Miscelaneous Sports")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Racing Sports")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Tennis")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("History")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Modern and classical arts")
                  .dateCreated(LocalDateTime.now())
                  .dateUpdated(null)
                  .build(),
        Topic.builder()
                .title("Music")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(null)
                .build(),
        Topic.builder()
                .title("Tv & Cinema")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(null)
                .build(),
        Topic.builder()
                .title("Healthy Life & Wellbeing")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(null)
                .build(),
        Topic.builder()
                .title("Medicine & Pandemics")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(null)
                .build(),
        Topic.builder()
                .title("Mental Health & Addictions")
                .dateCreated(LocalDateTime.now())
                .dateUpdated(null)
                .build()
        );

        logger.info("Created {} topics", topics.size());

        topicService.createAll(topics);

    }
}
