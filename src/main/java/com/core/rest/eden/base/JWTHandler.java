package com.core.rest.eden.base;

import com.core.rest.eden.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("JWT")
public class JWTHandler extends AbstractLogComponent implements CommandLineRunner {

    private final JWTService jwtService;

    @Override
    public void run(String... args) {

        /*Clear All*/
        logger.info("Flushing all Refresh Tokens");
        jwtService.findAll().forEach(token -> jwtService.deleteById(token.getId()));
    }
}
