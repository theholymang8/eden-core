package com.core.rest.eden.base;

import com.core.rest.eden.domain.Gender;
import com.core.rest.eden.domain.Role;
import com.core.rest.eden.domain.User;
import com.core.rest.eden.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("generate-users")
public class UsersFactory extends AbstractLogComponent implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {

        List<User> users = List.of(
                User.builder()
                    .email("giannisanast@outlook.com")
                    .firstName("Ioannis")
                    .lastName("Anastasopoulos")
                    .about("Passionate Developer who needs some time alone..")
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .roles(Set.of(Role.USER, Role.ADMIN))
                    .gender(Gender.MALE)
                    .dateOfBirth(LocalDate.of(1998, 9, 4))
                    .build(),
                User.builder()
                    .email("chmichail@outlook.com")
                    .firstName("Christos")
                    .lastName("Michail")
                    .about("Previous Developer..")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.MALE)
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1996, 4, 13))
                    .build(),
                User.builder()
                    .email("giorgoskomn@outlook.com")
                    .firstName("Giorgos")
                    .lastName("Komninos")
                    .about("Basketball Player that has an interest in Stocks...")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.MALE)
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1982, 11, 28))
                    .build(),
                User.builder()
                    .email("giannisanast@outlook.com")
                    .firstName("Xristina")
                    .lastName("Tziori")
                    .about("Passionate Model with a strive for technology")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.FEMALE)
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1986, 7, 2))
                    .build(),
                User.builder()
                    .email("stefanosmichail@outlook.com")
                    .firstName("Stefanos")
                    .lastName("Michalis")
                    .about("Gamer that spents most of his time scraping electronical components")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.MALE)
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1992, 9, 12))
                    .build(),
                User.builder()
                    .email("kostasmoul@outlook.com")
                    .firstName("Kostantinos")
                    .lastName("Moulopoulos")
                    .about("Soccer Player that wants to meet new people")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.MALE)
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1998, 3, 21))
                    .build(),
                User.builder()
                    .email("nikivalente@outlook.com")
                    .firstName("Niki")
                    .lastName("Valente")
                    .roles(Set.of(Role.USER))
                    .gender(Gender.FEMALE)
                    .about("I am a law school student with a hobby to photography.")
                    .password("$2a$12$/yJU9BvCcY2I5jyv/CxFGeOu6F0uJU0DeSQ2/vYqnRmKFg9egZYsa")
                    .dateOfBirth(LocalDate.of(1998, 2, 21))
                    .build()
        );

        userService.createAll(users);
        logger.info("Created {} users", users.size());
    }
}
