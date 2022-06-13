package com.core.rest.eden.base;

import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Profile("authentication-testing")
@RequiredArgsConstructor
public class AuthenticationTesting extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Override
    public void run(String... args) throws Exception {

        //userService.findByUsernameAuth("ganast");

        //userDetailsService.loadUserByUsername("ganast");

        userService.findByUsername("ganast");

    }
}
