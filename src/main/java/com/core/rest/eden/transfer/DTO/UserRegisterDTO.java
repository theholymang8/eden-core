package com.core.rest.eden.transfer.DTO;

import com.core.rest.eden.domain.Gender;
import com.core.rest.eden.domain.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserRegisterDTO {

    @NotEmpty
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String firstName;

    @NotEmpty
    @Size(min = 2, message = "Last Name should have at least 2 characters")
    private String lastName;

    @NotEmpty
    @Size(min = 4, message = "User should have at least 4 characters")
    private String userName;

    @NotEmpty
    @Size(min = 5, message = "Password should have at least 5 characters")
    private String password;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private LocalDate dateOfBirth;

    private String about;

    @NotNull
    private Gender gender;

    private Set<Topic> topics = new HashSet<>();

    private FileDTO avatar;
}
