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
@ToString(callSuper = true, exclude = {"comments", "user"})
@EqualsAndHashCode(callSuper = true, exclude = {"comments", "user"})
@Data

@Entity
@Table(name = "POSTS")

@JsonIgnoreProperties(value = {"user"})

@SequenceGenerator(name = "idGenerator", sequenceName = "POST_SEQ", allocationSize = 1)
public class Post extends BaseModel{

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


    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
                fetch = FetchType.LAZY,
                orphanRemoval = true,
    mappedBy = "post")
    @JsonView(Views.Public.class)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonView(Views.Detailed.class)
    private User user;

}
