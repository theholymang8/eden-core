package com.core.rest.eden.base;

import com.core.rest.eden.domain.Friendship;
import com.core.rest.eden.domain.FriendshipStatus;
import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.FriendshipService;
import com.core.rest.eden.services.FriendshipStatusService;
import com.core.rest.eden.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Profile("generate-friendships")
public class FriendshipFactory extends AbstractLogComponent implements CommandLineRunner {

    private final FriendshipStatusService friendshipStatusService;
    private final FriendshipService friendshipService;
    private final UserService userService;

    @Override
    public void run(String... args) {

        List<User> users = userService.findAll();

        List<FriendshipStatus> friendshipStatuses = List.of(
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(1))
                    .friendshipStatus(Status.REQUESTED)
                    .specifier(users.get(0))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(1))
                    .friendshipStatus(Status.ACCEPTED)
                    .specifier(users.get(1))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(2))
                    .addressee(users.get(3))
                    .friendshipStatus(Status.REQUESTED)
                    .specifier(users.get(2))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(2))
                    .addressee(users.get(3))
                    .friendshipStatus(Status.ACCEPTED)
                    .specifier(users.get(3))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(4))
                    .addressee(users.get(5))
                    .friendshipStatus(Status.REQUESTED)
                    .specifier(users.get(4))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(4))
                    .addressee(users.get(5))
                    .friendshipStatus(Status.REJECTED)
                    .specifier(users.get(5))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(5))
                    .friendshipStatus(Status.REQUESTED)
                    .specifier(users.get(0))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(5))
                    .friendshipStatus(Status.ACCEPTED)
                    .specifier(users.get(5))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(2))
                    .friendshipStatus(Status.REQUESTED)
                    .specifier(users.get(0))
                    .insertionTimestamp(LocalDateTime.now())
                    .build(),
            FriendshipStatus.builder()
                    .requester(users.get(0))
                    .addressee(users.get(2))
                    .friendshipStatus(Status.ACCEPTED)
                    .specifier(users.get(2))
                    .insertionTimestamp(LocalDateTime.now())
                    .build()
        );

        friendshipStatusService.createAll(friendshipStatuses);
        logger.info("Created {} friendship statuses", friendshipStatuses.size());

        List<Friendship> friendships = new ArrayList<>();

        friendshipStatusService.findAllByStatus(Status.ACCEPTED).forEach(acceptedStatus -> {
            friendships.add(
                    Friendship.builder()
                            .requester(acceptedStatus.getRequester())
                            .addressee(acceptedStatus.getAddressee())
                            .createdAt(acceptedStatus.getInsertionTimestamp())
                            .build()
            );
        });

        friendshipService.createAll(friendships);
        logger.info("Created {} friendships", friendships.size());

    }
}
