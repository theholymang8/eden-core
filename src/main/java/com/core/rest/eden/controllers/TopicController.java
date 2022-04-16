package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.Topic;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("topics")
public class TopicController extends AbstractController<Topic>{

    private final TopicService topicService;

    @Override
    protected BaseService<Topic, Long> getBaseService() {
        return topicService;
    }

    @GetMapping(path = "find", headers = "action=findByUsers")
    public ResponseEntity<ApiResponse<List<Topic>>> findByUsers(@RequestParam List<String> users){
        return ResponseEntity.ok(ApiResponse.<List<Topic>>builder()
                .data(topicService.findByUsers(users))
                .build());
    }

    @GetMapping(path = "find", headers = "action=findByTitle")
    public ResponseEntity<ApiResponse<Topic>> findByTitle(@RequestParam String title){
        return ResponseEntity.ok(ApiResponse.<Topic>builder()
                .data(topicService.findByTitle(title))
                .build());
    }
}
