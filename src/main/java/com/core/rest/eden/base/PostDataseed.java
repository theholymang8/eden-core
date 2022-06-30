package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.PostService;
import com.core.rest.eden.services.TopicService;
import com.core.rest.eden.services.UserService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Component
@Profile("post-dataseed")
public class PostDataseed extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;
    private final UserService userService;
    private final TopicService topicService;

    public void createPosts(Topic topic, String path) throws IOException, CsvValidationException {
        List<User> users = userService.findAll();
        Random rn = new Random();
        Integer max = users.size() - 1;
        Integer min = 0;

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }

        List<Post> posts = new ArrayList<>();

        records.forEach(record -> {
            Integer index = rn.nextInt(max-min)+1;
            //logger.info("Random Index: {}", index);
            posts.add(Post.builder()
                    .body(record.get(2))
                    .dateCreated(LocalDateTime.now())
                    .likes(0)
                    .topics(Set.of(topic))
                    .user(users.get(index))
                    .build());

        });

        postService.createAll(posts);
        logger.info("Created: {} posts related to {}", posts.size(), topic.getTitle());
    }

    @Override
    public void run(String... args) throws Exception {

        String techPath = "src/main/resources/dataseed/tweets_tech.csv";
        Topic techTopic = topicService.findByTitle("Technology");

        String cryptoPath = "src/main/resources/dataseed/tweets_crypto.csv";
        Topic cryptoTopic = topicService.findByTitle("Cryptocurrency");

        String politicsPath = "src/main/resources/dataseed/tweets_politics.csv";
        Topic politicsTopic = topicService.findByTitle("Politics");

        String sportsPath = "src/main/resources/dataseed/tweets_sports.csv";
        Topic sportsTopic = topicService.findByTitle("Sports");

        String musicPath = "src/main/resources/dataseed/tweets_music.csv";
        Topic musicTopic = topicService.findByTitle("Music");

        String envPath = "src/main/resources/dataseed/tweets_env.csv";
        Topic envTopic = topicService.findByTitle("Environment");

        String artPath = "src/main/resources/dataseed/tweets_art_and_culture.csv";
        Topic artTopic = topicService.findByTitle("Arts & Culture");

        String socialJustPath = "src/main/resources/dataseed/tweets_social_just.csv";
        Topic socialJustTopic = topicService.findByTitle("Activism");

        String healthAndLifestyle = "src/main/resources/dataseed/tweets_health_and_lifestyle.csv";
        Topic healthAndLifestyleTopic = topicService.findByTitle("Health & Lifestyle");

        String videoGamesPath = "src/main/resources/dataseed/tweets_video_games.csv";
        Topic videoGamesTopic = topicService.findByTitle("Video Games");

        this.createPosts(musicTopic, musicPath);
        this.createPosts(healthAndLifestyleTopic, healthAndLifestyle);
        this.createPosts(sportsTopic, sportsPath);
        this.createPosts(envTopic, envPath);
        this.createPosts(techTopic, techPath);
        this.createPosts(politicsTopic, politicsPath);
        this.createPosts(cryptoTopic, cryptoPath);
        this.createPosts(socialJustTopic, socialJustPath);
        this.createPosts(videoGamesTopic, videoGamesPath);
        this.createPosts(artTopic, artPath);


    }
}
