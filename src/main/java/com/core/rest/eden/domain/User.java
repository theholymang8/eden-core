package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.*;
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
@ToString(callSuper = true, exclude = {"groups", "comment", "files", "topics"})
@EqualsAndHashCode(callSuper = true, exclude = {"groups", "comment", "files", "topics"})
@Data

@Entity
@Table(name = "USERS")

@JsonIgnoreProperties(value = {"groups", "user", "comment"})


@SequenceGenerator(name = "idGenerator", sequenceName = "USER_SEQ", allocationSize = 1)
public class User extends BaseModel{

    @NotNull(message = "{username.null}")
    @Column(length = 60, nullable = false)
    @JsonView(Views.Public.class)
    private String username;

    @NotNull(message = "{firstName.null}")
    @Column(length = 160, nullable = false)
    @JsonView(Views.Public.class)
    private String firstName;

    @NotNull(message = "{lastName.null}")
    @Column(length = 300, nullable = false)
    @JsonView(Views.Public.class)
    private String lastName;

    @NotNull(message = "{dateOfBirth.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    @JsonView(Views.Detailed.class)
    private LocalDate dateOfBirth;

    @NotNull(message = "{email.null}")
    @Column(length = 180, nullable = false)
    @JsonView(Views.Detailed.class)
    private String email;

    @NotNull(message = "{password.null}")
    @Column(length = 2048, nullable = false)
    @JsonView(Views.Internal.class)
    private String password;

    @Column(length = 5000)
    @JsonView(Views.Detailed.class)
    private String about;

    @NotNull(message = "{gender.null}")
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Detailed.class)
    private Gender gender;

    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER,
            mappedBy = "user"
    )
    private Set<File> files;

    @OneToOne(
            mappedBy = "userAvatar",
            cascade = {CascadeType.ALL}
    )
    @JsonView(Views.Detailed.class)
    private File avatar;

    @NotNull(message = "{roles.null}")
    @ElementCollection(fetch = FetchType.EAGER, targetClass=Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ROLE")
    @JsonView(Views.Internal.class)
    private Set<Role> roles = new HashSet<>();


    @ManyToMany(
            mappedBy = "users",
            cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH},
            fetch = FetchType.EAGER
    )
    @JsonView(Views.Detailed.class)
    private Set<Topic> topics = new HashSet<>();


    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER,
            mappedBy = "user")
    @JsonView(Views.Detailed.class)
    private Set<Post> posts = new HashSet<>();


    @OneToOne(mappedBy = "user")
    @JsonView(Views.Internal.class)
    private Comment comment;

    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    @JsonView(Views.Detailed.class)
    private Set<Group> groups = new HashSet<>();

}
