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

    private final PostService postService;



    @Override
    public void run(String... args){

        List<User> users = userService.findAll();

        List<Topic> topics = List.of(
          Topic.builder()
                  .title("Bars")
                  .dateCreated(LocalDateTime.of(2022, 3, 2, 19, 37))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Privacy and Data")
                  .dateCreated(LocalDateTime.of(2022, 2, 1, 21, 18))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Social Media")
                  .dateCreated(LocalDateTime.of(2022, 3, 18, 13, 22))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Cryptocurrency")
                  .dateCreated(LocalDateTime.of(2022, 3, 13, 21, 21))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Political")
                  .dateCreated(LocalDateTime.of(2022, 3, 1, 22, 12))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Social Issues")
                  .dateCreated(LocalDateTime.of(2022, 1, 21, 18, 11))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Activism")
                  .dateCreated(LocalDateTime.of(2021, 12, 15, 17, 25))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("News")
                  .dateCreated(LocalDateTime.of(2022, 3, 2, 19, 37))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Technology")
                  .dateCreated(LocalDateTime.of(2021, 12, 14, 19, 11))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Trending")
                  .dateCreated(LocalDateTime.of(2022, 2, 17, 16, 32))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Marketing")
                  .dateCreated(LocalDateTime.of(2022, 1, 25, 14, 22))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Finance")
                  .dateCreated(LocalDateTime.of(2022, 1, 29, 15, 22))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Sports")
                  .dateCreated(LocalDateTime.of(2022, 1, 29, 15, 22))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Cinema")
                  .dateCreated(LocalDateTime.of(2021, 11, 22, 13, 13))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Arts")
                  .dateCreated(LocalDateTime.of(2022, 2, 22, 18, 7))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Theatre")
                  .dateCreated(LocalDateTime.of(2022, 2, 19, 13, 21))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Music")
                  .dateCreated(LocalDateTime.of(2022, 2, 21, 12, 18))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Video Games")
                  .dateCreated(LocalDateTime.of(2022, 2, 20, 11, 21))
                  .dateUpdated(null)
                  .build(),
          Topic.builder()
                  .title("Academia")
                  .dateCreated(LocalDateTime.of(2022, 1, 21, 16, 17))
                  .dateUpdated(null)
                  .build()
        );

        topics.forEach(topic -> logger.info("Topic : {}", topic));

        if (!users.isEmpty()) {
            logger.info("I exist.");
            topics.get(0).setUsers(Set.of(users.get(0)));

            topics.get(1).setUsers(Set.of(users.get(0)));

            topics.get(2).setUsers(Set.of(users.get(1)));

            topics.get(3).setUsers(Set.of(users.get(1)));

            topics.get(4).setUsers(Set.of(users.get(2)));

            topics.get(5).setUsers(Set.of(users.get(2)));

            topics.get(6).setUsers(Set.of(users.get(3)));

            topics.get(7).setUsers(Set.of(users.get(3)));

            topics.get(8).setUsers(Set.of(users.get(4)));

            topics.get(9).setUsers(Set.of(users.get(4)));

            topics.get(10).setUsers(Set.of(users.get(1)));

            topics.get(11).setUsers(Set.of(users.get(1)));

            topics.get(12).setUsers(Set.of(users.get(1)));

            topics.get(13).setUsers(Set.of(users.get(0)));

            topics.get(14).setUsers(Set.of(users.get(0)));

            topics.get(15).setUsers(Set.of(users.get(2)));

            topics.get(16).setUsers(Set.of(users.get(2)));

            topics.get(17).setUsers(Set.of(users.get(3)));

        }

        logger.info("Created {} topics", topics.size());

        //topics.forEach(topic -> {
           // logger.info("Topic : {}", topic);
        //});

        topicService.createAll(topics);

    }
}
