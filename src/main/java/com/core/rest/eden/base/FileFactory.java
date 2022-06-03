package com.core.rest.eden.base;

import com.core.rest.eden.domain.File;
import com.core.rest.eden.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Profile("generate-files")
public class FileFactory extends AbstractLogComponent implements CommandLineRunner {

    private final FileService fileService;

    @Override
    public void run(String... args) throws Exception {

        /* Create a default image for users who have not chosen an avatar */
        /*fileService.create(File.builder()
                .
                .build());*/
    }
}
