package com.core.rest.eden.services;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.repositories.FriendshipRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl extends BaseServiceImpl<Friendship> implements FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Override
    public JpaRepository<Friendship, Long> getRepository() {
        return friendshipRepository;
    }

    @Override
    public List<Friendship> findAllByRequester(User user) {
        return friendshipRepository.findAllByRequester(user);
    }

    @Override
    public List<Friendship> findAllByAddressee(User user) {
        return friendshipRepository.findAllByAddressee(user);
    }
}
