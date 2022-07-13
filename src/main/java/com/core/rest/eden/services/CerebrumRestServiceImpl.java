package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.exceptions.RecommenderServiceHTTPException;
import com.core.rest.eden.transfer.DTO.RecommendedFriendsDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;


@Service
public class CerebrumRestServiceImpl extends AbstractLogComponent implements CerebrumRestService {

    private final RestTemplate restTemplate;
    private static final String friendsRecommender = "http://localhost:4000/friends/recommend";

    public CerebrumRestServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public RecommendedFriendsDTO getRecommendedFriends(Long userId) {

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //Build the HTTP Request
        HttpEntity request = new HttpEntity(headers);

        logger.info("User id : {}", userId);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(friendsRecommender)
                .queryParam("user", "{user}")
                .encode()
                .toUriString();

        Map<String, ?> params = Map.of(
                "user", userId.toString()
        );

        ResponseEntity<RecommendedFriendsDTO> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                RecommendedFriendsDTO.class,
                params
        );

        logger.info("Params: {}", params);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Recommender Service HTTP Exception", new Exception("Recommender Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }
        logger.info("User's Interests: {}", response.getBody());
        return response.getBody();

    }
}
