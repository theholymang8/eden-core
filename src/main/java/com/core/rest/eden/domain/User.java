package com.core.rest.eden.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"groups", "comment"})
@EqualsAndHashCode(callSuper = true, exclude = {"groups", "comment"})
@Data

@Entity
@Table(name = "USERS")

@JsonIgnoreProperties(value = {"groups", "user", "comment"})

@SequenceGenerator(name = "idGenerator", sequenceName = "USER_SEQ", allocationSize = 1)
public class User extends BaseModel{

    @NotNull(message = "{firstName.null}")
    @Column(length = 160, nullable = false)
    private String firstName;

    @NotNull(message = "{lastName.null}")
    @Column(length = 300, nullable = false)
    private String lastName;

    @NotNull(message = "{dateOfBirth.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotNull(message = "{email.null}")
    @Column(length = 180, nullable = false)
    private String email;

    @NotNull(message = "{password.null}")
    @Column(length = 2048, nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 5000)
    private String about;

    @NotNull(message = "{gender.null}")
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "{roles.null}")
    @ElementCollection(fetch = FetchType.EAGER, targetClass=Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ROLE")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER,
            mappedBy = "user")
    private Set<Topic> topics = new HashSet<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY,
            mappedBy = "user")
    private Set<Post> posts = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Comment comment;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();
}
