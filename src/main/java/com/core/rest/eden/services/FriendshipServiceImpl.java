package com.core.rest.eden.services;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.exceptions.AlreadyFriendsException;
import com.core.rest.eden.repositories.FriendshipRepository;
import com.core.rest.eden.transfer.DTO.FriendRequestDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class FriendshipServiceImpl extends BaseServiceImpl<Friendship> implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final FriendshipStatusService friendshipStatusService;

    @Override
    public JpaRepository<Friendship, Long> getRepository() {
        return friendshipRepository;
    }

    @Override
    public Friendship acceptFriendship(User requester, User addressee) throws AlreadyFriendsException{
        Friendship friendship = friendshipRepository.isAlreadyFriends(requester, addressee);
        if (friendship != null) {
            logger.info("Already Friends");
        throw new AlreadyFriendsException("Already friends exception", new Exception("Custom Already Friends Exception"));
        }
        friendshipStatusService.acceptFriendRequest(requester, addressee);
        return friendshipRepository.save(
                Friendship.builder()
                    .requester(requester)
                    .addressee(addressee)
                    .createdAt(LocalDateTime.now())
                    .build()
        );
    }

    @Override
    public Friendship isAlreadyFriends(User requester, User addressee) {
        //logger.info("Already Friends: {}", friendshipRepository.isAlreadyFriends(requester, addressee));
        //return (friendshipRepository.isAlreadyFriends(requester, addressee) == null) ? friendshipRepository.isAlreadyFriends(addressee, requester) : friendshipRepository.isAlreadyFriends(requester, addressee);
        return friendshipRepository.isAlreadyFriends(requester, addressee);
    }

    @Override
    public Boolean hasSentRequest(User requester, User addressee) {
        return friendshipStatusService.hasSentRequest(requester, addressee);
    }

    @Override
    public void requestFriendship(User requester, User addressee) {
        friendshipStatusService.newFriendRequest(requester, addressee);
    }

    @Override
    public void rejectFriendship(User requester, User addressee) {
        friendshipStatusService.rejectFriendRequest(requester, addressee);
    }

    @Override
    public void deleteFriendship(User requester, User addressee) {
        Friendship friendship = friendshipRepository.isAlreadyFriends(requester, addressee);
        if (friendship != null) {
            //needs change to new repository method
            friendshipStatusService.deleteFriendship(requester, addressee);
            friendshipRepository.delete(friendshipRepository.findByRequesterAndAddressee(requester, addressee));
        }else {
            logger.error("This friendship does not exist");
        }
    }

    @Override
    public List<User> getAllFriendRequests(User addressee) {
        //return null;
        return friendshipStatusService.getAllFriendRequests(addressee);
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
