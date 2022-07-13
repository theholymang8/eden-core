package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.exceptions.RecommenderServiceHTTPException;
import com.core.rest.eden.transfer.DTO.PostTopicsDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
public class GetTopicPostsServiceImpl extends AbstractLogComponent implements GetTopicPostsService {

    private static String url = "http://localhost:5000/find/topics";

    private final RestTemplate restTemplate;

    public GetTopicPostsServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public PostTopicsDTO getUserPreferencedTopics(Set<Topic> userTopics) throws RecommenderServiceHTTPException{
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        List<String> interests = new ArrayList<>();
        userTopics.forEach(topic -> {
            interests.add(topic.getTitle());
        });

        Map<String,List<String>> postBody = Map.of("user_interests", interests);

        HttpEntity<Map<String,List<String>>> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<PostTopicsDTO> response = restTemplate.postForEntity(url, entity, PostTopicsDTO.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Recommender Service HTTP Exception", new Exception("Recommender Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }
        logger.info("User's Interests: {}", response.getBody());
        return response.getBody();
    }
}
