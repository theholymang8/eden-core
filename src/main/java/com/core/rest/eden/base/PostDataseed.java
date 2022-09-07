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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Component
@Profile("post-dataseed")
public class PostDataseed extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;
    private final UserService userService;
    private final TopicService topicService;
    private final ZoneId zone = ZoneId.of("Europe/Athens");

    public void createPosts(Set<Topic> topics, String path, Integer topicIndex, Integer bodyIndex) throws IOException, CsvValidationException {
        List<User> users = userService.findByTopics(topics);
        Random rn = new Random();
        Integer max = users.size() - 1;
        Integer min = 0;

        long minDay = LocalDateTime.of(2022, 1, 1,23,0).toEpochSecond(zone.getRules().getOffset(LocalDateTime.now()));
        long maxDay = LocalDateTime.of(2022, 7, 6,23,0).toEpochSecond(zone.getRules().getOffset(LocalDateTime.now()));

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
            records.remove(0); //remove header from read values
        }

        List<Post> posts = new ArrayList<>();

        records.forEach(record -> {
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomDay, 0, zone.getRules().getOffset(LocalDateTime.now()));

            Integer index = rn.nextInt(max-min)+1;

            Topic postTopic = topicService.findByTitle(record.get(topicIndex));
            posts.add(Post.builder()
                    .body(record.get(bodyIndex))
                    .dateCreated(randomDate)
                    .likes(0)
                    .topics(Set.of(postTopic))
                    .clusteredTopic(Integer.valueOf(record.get(4)))
                    .user(users.get(index))
                    .build());

        });

        postService.createAll(posts);
        logger.info("Created: {} posts", posts.size());
    }

    @Override
    public void run(String... args) throws Exception {

        String techPath = "src/main/resources/dataseed/tech_posts.csv";
        Set<Topic> techTopics = new HashSet<>();
        techTopics.add(topicService.findByTitle("Artificial Intelligence"));
        techTopics.add(topicService.findByTitle("Cryptocurrencies"));
        techTopics.add(topicService.findByTitle("General Technology News"));
        techTopics.add(topicService.findByTitle("Space & Science"));
        techTopics.add(topicService.findByTitle("Technology Products"));
        this.createPosts(techTopics, techPath, 3, 1);

        String politicsPath = "src/main/resources/dataseed/politics_posts.csv";
        Set<Topic> politicsTopics = new HashSet<>();
        politicsTopics.add(topicService.findByTitle("Activism"));
        politicsTopics.add(topicService.findByTitle("Economy"));
        politicsTopics.add(topicService.findByTitle("Environment"));
        politicsTopics.add(topicService.findByTitle("Health & Politics"));
        politicsTopics.add(topicService.findByTitle("International & Foreign Politics"));
        politicsTopics.add(topicService.findByTitle("Miscelaneous Politics & Legislation"));
        this.createPosts(politicsTopics, politicsPath, 3 ,1);

        String sportsPath = "src/main/resources/dataseed/sports_posts.csv";
        Set<Topic> sportsTopics = new HashSet<>();
        sportsTopics.add(topicService.findByTitle("Basketball & NBA"));
        sportsTopics.add(topicService.findByTitle("Football"));
        sportsTopics.add(topicService.findByTitle("General & Miscelaneous Sports"));
        sportsTopics.add(topicService.findByTitle("Racing Sports"));
        sportsTopics.add(topicService.findByTitle("Tennis"));
        this.createPosts(sportsTopics, sportsPath,3 ,1);

        String culturePath = "src/main/resources/dataseed/culture_posts.csv";
        Set<Topic> cultureTopics = new HashSet<>();
        cultureTopics.add(topicService.findByTitle("History"));
        cultureTopics.add(topicService.findByTitle("Modern and classical arts"));
        cultureTopics.add(topicService.findByTitle("Music"));
        cultureTopics.add(topicService.findByTitle("TV & Cinema"));
        this.createPosts(cultureTopics, culturePath,3 ,1);

        String healthPath = "src/main/resources/dataseed/health_posts.csv";
        Set<Topic> healthTopics = new HashSet<>();
        healthTopics.add(topicService.findByTitle("Healthy Life & Wellbeing"));
        healthTopics.add(topicService.findByTitle("Medicine & Pandemics"));
        healthTopics.add(topicService.findByTitle("Mental Health & Addictions"));
        this.createPosts(healthTopics, healthPath,3 ,1);



    }
}
