package com.core.rest.eden.services;

import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.exceptions.UserHasAlreadySentRequestException;
import com.core.rest.eden.repositories.FriendshipStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public FriendshipStatus newFriendRequest(User requester, User addressee) throws UserHasAlreadySentRequestException{
        if (checkIfRequestIsSent(requester, addressee)) {
            throw new UserHasAlreadySentRequestException("User has already sent a friend request", new Exception("Custom Friend Request Exception"));
        }
        return friendshipStatusRepository.save(
               FriendshipStatus.builder()
                    .requester(requester)
                    .addressee(addressee)
                    .specifier(requester)
                    .insertionTimestamp(LocalDateTime.now())
                    .friendshipStatus(Status.REQUESTED)
                    .build()
        );
    }

    @Override
    public FriendshipStatus acceptFriendRequest(User requester, User addressee) {
        if (checkIfRequestIsAccepted(requester, addressee)) {
            return null;
        }
        FriendshipStatus friendshipStatus = friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, addressee, Status.REQUESTED);
        friendshipStatus.setFriendshipStatus(Status.ACCEPTED);
        friendshipStatus.setInsertionTimestamp(LocalDateTime.now());
        friendshipStatus.setSpecifier(addressee);
        return friendshipStatusRepository.save(friendshipStatus);
    }

    @Override
    public FriendshipStatus rejectFriendRequest(User requester, User addressee) {
        if (checkIfRequestIsReject(requester, addressee)) {
            return null;
        }
        FriendshipStatus friendshipStatus = friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, addressee, Status.REQUESTED);
        friendshipStatus.setFriendshipStatus(Status.REJECTED);
        friendshipStatus.setInsertionTimestamp(LocalDateTime.now());
        friendshipStatus.setSpecifier(addressee);
        return friendshipStatusRepository.save(friendshipStatus);
    }

    @Override
    public FriendshipStatus deleteFriendRequest(User requester, User addressee) {
        if (checkIfRequestIsReject(requester, addressee)) {
            return null;
        }
        FriendshipStatus friendshipStatus = friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, addressee, Status.REQUESTED);
        friendshipStatus.setFriendshipStatus(Status.DELETED);
        friendshipStatus.setInsertionTimestamp(LocalDateTime.now());
        friendshipStatus.setSpecifier(addressee);
        return friendshipStatusRepository.save(friendshipStatus);
    }

    @Override
    public Boolean hasSentRequest(User requester, User addressee) {
        return checkIfRequestIsSent(requester, addressee);
    }

    @Override
    public FriendshipStatus blockFriendRequest(User requester, User addressee) {
        if (checkIfRequestIsReject(requester, addressee)) {
            return null;
        }
        FriendshipStatus friendshipStatus = friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, addressee, Status.REQUESTED);
        friendshipStatus.setFriendshipStatus(Status.BLOCKED);
        friendshipStatus.setInsertionTimestamp(LocalDateTime.now());
        friendshipStatus.setSpecifier(addressee);
        return friendshipStatusRepository.save(friendshipStatus);
    }

    @Override
    public List<User> getAllFriendRequests(User addressee) {
        List<Status> statuses = Arrays.asList(
                Status.ACCEPTED,
                Status.BLOCKED,
                Status.REJECTED,
                Status.DELETED,
                Status.PENDING);
        return friendshipStatusRepository.findAllFriendRequests(addressee, statuses);
    }

    @Override
    public List<FriendshipStatus> findAllByStatus(Status status) {
        return friendshipStatusRepository.findAllByFriendshipStatus(status);
    }

    @Override
    public List<FriendshipStatus> findAllBySpecifier(User user) {
        return friendshipStatusRepository.findAllBySpecifier(user);
    }

    public Boolean checkIfRequestIsSent(User requester, User Addressee) {
        return friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, Addressee, Status.REQUESTED) != null;
    }

    public Boolean checkIfRequestIsAccepted(User requester, User Addressee) {
        return friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, Addressee, Status.ACCEPTED) != null;
    }

    public Boolean checkIfRequestIsReject(User requester, User Addressee) {
        return friendshipStatusRepository.findByRequesterAndAddresseeAndFriendshipStatus(requester, Addressee, Status.REJECTED) != null;
    }
}
