package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
@ToString(callSuper = true, exclude = {"files", "userBehavior", "userTopicScores"})
@EqualsAndHashCode(callSuper = true, exclude = {"files", "userBehavior", "userTopicScores"})
@Data

@Entity
@Table(name = "USERS")

@JsonIgnoreProperties(value = {"groups", "user", "comment"})

@SequenceGenerator(name = "idGenerator", sequenceName = "USER_SEQ", allocationSize = 1)
public class User extends BaseModel{

    @NotNull(message = "{username.null}")
    @Column(length = 60, nullable = false)
    @JsonView(Views.Public.class)
    @JsonProperty("userName")
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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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

    @JsonView(Views.Relational.class)
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY,
            mappedBy = "user"
    )
    private Set<File> files;


    @OneToOne(
            mappedBy = "userAvatar",
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY
    )
    @JsonView(Views.Public.class)
    private File avatar;

    @NotNull(message = "{roles.null}")
    @ElementCollection(fetch = FetchType.EAGER, targetClass=Role.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "ROLE")
    @JsonView(Views.Internal.class)
    private Set<Role> roles = new HashSet<>();


    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<UserPostBehaviour> userBehavior = new HashSet<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<UserTopicScore> userTopicScores = new HashSet<>();

    @ManyToMany(
            cascade = {CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "`USER_TOPICS`",
            joinColumns = @JoinColumn(name = "`user_id`"),
            foreignKey = @ForeignKey(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "`topic_id`"),
            inverseForeignKey = @ForeignKey(name = "topic_id")
    )
    @JsonView(Views.Detailed.class)
    private Set<Topic> topics = new HashSet<>();


    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @OrderBy("dateCreated DESC")
    @JsonView(Views.Relational.class)
    private Set<Post> posts = new HashSet<>();

}
