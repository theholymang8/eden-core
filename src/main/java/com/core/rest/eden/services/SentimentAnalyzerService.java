package com.core.rest.eden.services;

import com.core.rest.eden.domain.Comment;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.domain.Sentiment;

public interface SentimentAnalyzerService {

    Sentiment analyzeComment(Comment comment);

    Sentiment analyzePost(Post post);
    //List<>

}
