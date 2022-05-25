package com.core.rest.eden.services;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.FriendshipStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendshipStatusServiceImpl extends BaseServiceImpl<FriendshipStatus> implements FriendshipStatusService {

    private final FriendshipStatusRepository friendshipStatusRepository;

    @Override
    public JpaRepository<FriendshipStatus, Long> getRepository() {
        return friendshipStatusRepository;
    }

    @Override
    public List<FriendshipStatus> findAllByStatus(Status status) {
        return friendshipStatusRepository.findAllByFriendshipStatus(status);
    }

    @Override
    public List<FriendshipStatus> findAllBySpecifier(User user) {
        return friendshipStatusRepository.findAllBySpecifier(user);
    }
}
