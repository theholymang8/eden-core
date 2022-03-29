package com.core.rest.eden.services;

import com.core.rest.eden.domain.Group;
import com.core.rest.eden.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends BaseServiceImpl<Group> implements GroupService{

    private final GroupRepository groupRepository;

    @Override
    public JpaRepository<Group, Long> getRepository() {
        return groupRepository;
    }
}
