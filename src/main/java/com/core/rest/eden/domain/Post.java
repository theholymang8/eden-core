package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"comments", "postBehavior"})
@EqualsAndHashCode(callSuper = true, exclude = {"comments", "postBehavior"})
@Data

@Entity
@Table(name = "POSTS")



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


    @ManyToMany(
            cascade = {CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "`POST_TOPICS`",
            joinColumns = @JoinColumn(name = "`post_id`"),
            foreignKey = @ForeignKey(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "`topic_id`"),
            inverseForeignKey = @ForeignKey(name = "topic_id")
    )
    @JsonView(Views.Detailed.class)
    private Set<Topic> topics = new HashSet<>();


    @Column
    @JsonView(Views.Detailed.class)
    private Sentiment postSentiment;

    @Column(name = "clustered_topic")
    private Integer clusteredTopic;

    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonView(Views.Relational.class)
    private Set<UserPostBehaviour> postBehavior = new HashSet<>();


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
