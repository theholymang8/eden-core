package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.DTO.CommentView;
import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"post"})
@EqualsAndHashCode(callSuper = true, exclude = {"post"})
@Data

@JsonIgnoreProperties(value = {"post"})

@Entity
@Table(name = "COMMENTS")

@SequenceGenerator(name = "idGenerator", sequenceName = "COMMENT_SEQ", allocationSize = 1)
public class Comment extends BaseModel{

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

    @ManyToOne
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment")
    private Sentiment sentiment;

    @OneToOne
    @JsonView(Views.Public.class)
    @Getter(AccessLevel.NONE)
    private User user;

    public String getUser() {
        return this.user.getUsername();
    }
}
