package com.core.rest.eden.services;

import com.core.rest.eden.transfer.DTO.NewsDTO;

import java.util.List;
import java.util.concurrent.Future;

public interface NewsRecommendationService {

    Future<List<NewsDTO>> getNews(String query);

}
