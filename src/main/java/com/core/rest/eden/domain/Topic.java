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
@ToString(callSuper = true, exclude = {"user"})
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@Data

@JsonIgnoreProperties(value = {"user"})

@Entity
@Table(name = "TOPICS")

@SequenceGenerator(name = "idGenerator", sequenceName = "TOPIC_SEQ", allocationSize = 1)
public class Topic extends BaseModel{

    @NotNull(message = "{title.null}")
    @Column(length = 1024, nullable = false)
    private String title;

    @NotNull(message = "{dateCreated.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(nullable = true)
    private LocalDateTime dateUpdated;

    @ManyToOne
    private User user;

}
