package com.core.rest.eden.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column
    private LocalDateTime dateUpdated;

    @NotNull(message = "{body.null}")
    @Column(length = 5000, nullable = false)
    private String body;

    @ManyToOne
    private Post post;

    @OneToOne
    private User user;

}
