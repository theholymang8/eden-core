package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Sentiment;
import com.core.rest.eden.exceptions.RecommenderServiceHTTPException;
import com.core.rest.eden.transfer.DTO.EntitySentimentDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Service
public class SentimentAnalyzeServiceImpl extends AbstractLogComponent implements SentimentAnalyzerService {


    private static String url = "http://localhost:6000/entity/analyze";

    private final RestTemplate restTemplate;

    public SentimentAnalyzeServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Sentiment analyzeComment(Comment comment) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String,String> postBody = Map.of("entity", comment.getBody());

        HttpEntity<Map<String,String>> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<EntitySentimentDTO> response = this.restTemplate.postForEntity(url, entity, EntitySentimentDTO.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Analyzer Service HTTP Exception", new Exception("Analyzer Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }
        logger.info("User's Interests: {}", response.getBody());

        return getSentiment(response);
    }

    @Override
    public Sentiment analyzePost(Post post) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String,String> postBody = Map.of("entity", post.getBody());

        HttpEntity<Map<String,String>> entity = new HttpEntity<>(postBody, headers);
        ResponseEntity<EntitySentimentDTO> response = this.restTemplate.postForEntity(url, entity, EntitySentimentDTO.class);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Analyzer Service HTTP Exception", new Exception("Analyzer Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }

        return getSentiment(response);
    }

    private Sentiment getSentiment(ResponseEntity<EntitySentimentDTO> response) {

        EntitySentimentDTO sentimentList = response.getBody();

        Map<Sentiment, Float> sentimentValues = Map.of(
                Sentiment.POSITIVE, sentimentList.getPositive(),
                Sentiment.NEUTRAL, sentimentList.getNeutral(),
                Sentiment.NEGATIVE, sentimentList.getNegative()
                );
        return maxOfMap(sentimentValues);
    }

    public <K, V extends Comparable<V>> K maxOfMap(Map<K, V> map) {
        Map.Entry<K, V> maxEntry = Collections.max(map.entrySet(),
                Map.Entry.comparingByValue());
        return maxEntry.getKey();
    }
}
