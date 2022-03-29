package com.core.rest.eden.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column
    private LocalDateTime dateUpdated;

    @NotNull(message = "{body.null}")
    @Column(length = 5000, nullable = false)
    private String body;

    @Column
    private Integer likes;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
                fetch = FetchType.LAZY,
                orphanRemoval = true,
    mappedBy = "post")
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    private User user;

}
