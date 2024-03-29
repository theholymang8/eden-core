package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"users", "posts"})
@EqualsAndHashCode(callSuper = true, exclude = {"users", "posts"})
@Data

@JsonIgnoreProperties(value = {"users", "posts"})

@Entity
@Table(name = "TOPICS")

@SequenceGenerator(name = "idGenerator", sequenceName = "TOPIC_SEQ", allocationSize = 1)
public class Topic extends BaseModel{

    @NotNull(message = "{title.null}")
    @Column(length = 1024, nullable = false)
    @JsonView(Views.Public.class)
    private String title;

    @NotNull(message = "{dateCreated.null}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(nullable = false)
    @JsonView(Views.Detailed.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss")
    @Column(nullable = true)
    @JsonView(Views.Detailed.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateUpdated;


    @ManyToMany(mappedBy = "topics",
            fetch = FetchType.LAZY
    )
    @JsonView(Views.Public.class)
    private Set<Post> posts = new HashSet<>();


    @ManyToMany(
            mappedBy = "topics",
            cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH},
            fetch = FetchType.LAZY
    )
    @JsonView(Views.Detailed.class)
    private Set<User> users = new HashSet<>();

}
