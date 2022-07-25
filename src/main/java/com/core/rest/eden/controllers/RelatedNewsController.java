package com.core.rest.eden.controllers;

import com.core.rest.eden.base.AbstractLogComponent;
import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.services.UserService;
import com.core.rest.eden.transfer.DTO.NewsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class RelatedNewsController extends AbstractLogComponent {

    private final UserService userService;

    @GetMapping(
                path = "related",
                params = "username",
                headers = "action=getRelatedNews")
    public ResponseEntity<ApiResponse<List<NewsDTO>>> getRelatedNews(@RequestParam("username") String username) {
        return ResponseEntity.ok(ApiResponse.<List<NewsDTO>>builder()
                .data(userService.getRelatedNews(username))
                .build());
    }

}
