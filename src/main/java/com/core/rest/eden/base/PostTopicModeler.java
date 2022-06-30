package com.core.rest.eden.base;

import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.PostService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
@Profile("post-topic-modeler")
public class PostTopicModeler extends AbstractLogComponent implements CommandLineRunner {

    private final PostService postService;

    public void readPosts(String path) throws IOException, CsvValidationException {

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                if(values[1].equals("id")) continue;
                records.add(Arrays.asList(values));
            }
        }


        records.forEach(record -> {
            Long postId = Long.parseLong(record.get(1));
            Integer topicId = Integer.parseInt(record.get(3));
            Post post = postService.findById(postId);
            post.setClustered_topic(topicId);
            postService.update(post);
        });


    }

    @Override
    public void run(String... args) throws Exception {
        String path = "src/main/resources/dataseed/topic_modeled_posts.csv";
        this.readPosts(path);

    }
}
