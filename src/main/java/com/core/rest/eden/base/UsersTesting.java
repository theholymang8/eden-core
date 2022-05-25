package com.core.rest.eden.base;

import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Profile("user-testing")
public class UsersTesting extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userService.findAll();
        users.forEach(user -> logger.info("User: {}", user.getTopics()));

        userService.find(1L).getTopics().forEach(topic -> logger.info("User has these topics: {}"));

    }
}
