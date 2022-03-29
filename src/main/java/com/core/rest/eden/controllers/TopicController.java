package com.core.rest.eden.controllers;

import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class TopicController extends AbstractController<Topic>{

    private final TopicService topicService;

    @Override
    protected BaseService<Topic, Long> getBaseService() {
        return topicService;
    }
}
