package com.core.rest.eden.controllers;

import com.core.rest.eden.domain.Group;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("groups")
public class GroupController extends AbstractController<Group>{

    private final GroupService groupService;

    @Override
    protected BaseService<Group, Long> getBaseService() {
        return groupService;
    }
}
