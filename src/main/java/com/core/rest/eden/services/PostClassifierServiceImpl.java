package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.exceptions.RecommenderServiceHTTPException;
import com.core.rest.eden.transfer.DTO.ClassifiedPostDTO;
import com.core.rest.eden.transfer.DTO.ClusteredPostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Future;

@Service
public class PostClassifierServiceImpl extends AbstractLogComponent implements PostClassifierService {


    private static String url = "http://localhost:6000/predict";
    private static String clusterUrl = "http://localhost:5000/cluster/post";


    private final TopicService topicService;


    @Autowired
    public PostClassifierServiceImpl(RestTemplateBuilder restTemplateBuilder, TopicService topicService){
        this.restTemplate = restTemplateBuilder.build();
        this.topicService = topicService;
    }

    private final RestTemplate restTemplate;



    @Override
    public Post classifyPost(Post post) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String,String> postBody = Map.of("post_to_classify", post.getBody());

        HttpEntity<Map<String,String>> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<ClassifiedPostDTO> response = this.restTemplate.postForEntity(url, entity, ClassifiedPostDTO.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Classifier Service HTTP Exception", new Exception("Classifier Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }

        ClassifiedPostDTO classifiedTopic = response.getBody();

        Topic foundTopic = topicService.findByTitle(classifiedTopic.getTopic());

        post.setTopics(Set.of(foundTopic));

        return post;

    }

    @Override
    @Async
    public Future<Post> clusterPost(Post post) {

        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String,String> postBody = Map.of("post_to_cluster", post.getBody());

        HttpEntity<Map<String,String>> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<ClusteredPostDTO> response = this.restTemplate.postForEntity(clusterUrl, entity, ClusteredPostDTO.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Classifier Service HTTP Exception", new Exception("Classifier Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }

        ClusteredPostDTO clusteredPost = response.getBody();

        post.setClusteredTopic(clusteredPost.getCluster());


        return new AsyncResult<>(post);
    }
}
