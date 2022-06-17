package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"comments"})
@EqualsAndHashCode(callSuper = true, exclude = {"comments"})
@Data

@Entity
@Table(name = "POSTS")

//@JsonIgnoreProperties(value = {"user"})

@SequenceGenerator(name = "idGenerator", sequenceName = "POST_SEQ", allocationSize = 1)
public class Post extends BaseModel implements Comparable<Post>{

    @NotNull(message = "{dateCreated.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(nullable = false)
    @JsonView(Views.Public.class)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column
    @JsonView(Views.Public.class)
    private LocalDateTime dateUpdated;

    @NotNull(message = "{body.null}")
    @Column(length = 5000, nullable = false)
    @JsonView(Views.Public.class)
    private String body;

    @Column
    @JsonView(Views.Public.class)
    private Integer likes;


    @ManyToMany(mappedBy = "posts",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JsonView(Views.Public.class)
    private Set<Topic> topics = new HashSet<>();

    /*@JsonView(Views.Public.class)
    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY,
            mappedBy = "post"
    )
    private Set<File> files;*/

    @JsonView(Views.Public.class)
    @OneToOne(cascade = CascadeType.ALL,
              orphanRemoval = true
    )
    //@PrimaryKeyJoinColumn
    private File image;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
                fetch = FetchType.LAZY,
                orphanRemoval = true,
    mappedBy = "post")
    @JsonView(Views.Public.class)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonView(Views.Public.class)
    @Getter(AccessLevel.NONE)
    private User user;


    @Override
    public int compareTo(Post post) {
        if (getDateCreated() == null || post.getDateCreated() == null) {
            return 0;
        }
        return getDateCreated().compareTo(post.getDateCreated());
    }

    public String getUser() {
        return this.user.getUsername();
    }
}
