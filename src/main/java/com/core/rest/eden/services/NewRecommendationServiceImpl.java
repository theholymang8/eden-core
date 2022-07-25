package com.core.rest.eden.services;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.exceptions.RecommenderServiceHTTPException;
import com.core.rest.eden.transfer.DTO.NewsDTO;
import com.core.rest.eden.transfer.DTO.NewsResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class NewRecommendationServiceImpl extends AbstractLogComponent implements NewsRecommendationService {

    private final RestTemplate restTemplate;
    private static final String newsUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json?";

    @Value("${nyt.api.key}")
    private String apiKey;

    public NewRecommendationServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @Async
    public Future<List<NewsDTO>> getNews(String query) {

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //Build the HTTP Request
        HttpEntity request = new HttpEntity(headers);

        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        String beginDate = startDate.toString().replace("-", "");

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(newsUrl)
                .queryParam("q", "{q}")
                .queryParam("api-key", "{api-key}")
                .queryParam("sort", "{sort}")
                .queryParam("begin_date", "{begin_date}")
                .encode()
                .toUriString();

        //logger.info("Api Key: {}", apiKey);

        Map<String, ?> params = Map.of(
                "q", query,
                "sort", "relevance",
                "begin_date", beginDate,
                "api-key", apiKey
        );

        //logger.info("Params: {}", params);
        //logger.info("Params: {}", urlTemplate);


        ResponseEntity<NewsResponseDTO> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                request,
                NewsResponseDTO.class,
                params
        );

        //logger.info("Params: {}", params);

        if (response.getStatusCode() != HttpStatus.OK){
            throw new RecommenderServiceHTTPException("Recommender Service HTTP Exception", new Exception("Recommender Service HTTP Error Exception with error code: "+response.getStatusCode()));
        }
        NewsResponseDTO responseBody = response.getBody();

        List<NewsDTO> news = responseBody.getResponse().getDocs();
        //logger.info("News: {}", news);
        return new AsyncResult<>(news);
    }
}
