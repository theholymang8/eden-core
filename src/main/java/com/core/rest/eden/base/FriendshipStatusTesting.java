package com.core.rest.eden.base;

import com.core.rest.eden.domain.Status;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.FriendshipStatusService;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("friend-request-testing")
public class FriendshipStatusTesting extends AbstractLogComponent implements CommandLineRunner {

    private final FriendshipStatusService friendshipStatusService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        User user = userService.find(73L);

        List<User> users = friendshipStatusService.getAllFriendRequests(user);

        users.forEach(userL -> {
            logger.info("This user has sent you a friend request: {} - {}",
                    userL.getUsername(),
                    userL.getEmail());
        });
    }
}
