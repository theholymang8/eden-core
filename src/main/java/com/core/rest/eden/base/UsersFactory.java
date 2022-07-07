package com.core.rest.eden.base;

import com.core.rest.eden.domain.*;
import com.core.rest.eden.services.UserService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
@Profile("generate-users")
public class UsersFactory extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    public void createUsers(String path) throws IOException, CsvValidationException {

        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader(path));) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
            records.remove(0); //remove header from read values
        }

        List<User> users = new ArrayList<>();

        records.forEach(record -> {
            users.add(User.builder()
                    .username(record.get(0))
                    .email(record.get(3))
                    .firstName(record.get(1))
                    .lastName(record.get(2))
                    //.about("Passionate Developer who needs some time alone..")
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.valueOf(record.get(4)))
                    .dateOfBirth(LocalDate.parse(record.get(6)))
                    .build());
        });

        userService.createAll(users);
        logger.info("Created: {} users", users.size());
    }

    @Override
    public void run(String... args) throws CsvValidationException, IOException {

        String usersPath = "src/main/resources/dataseed/users_dataseed.csv";
        this.createUsers(usersPath);
    }
}
