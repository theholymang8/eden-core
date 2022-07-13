package com.core.rest.eden.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

@Entity
@Table(name = "USER_POST_BEHAVIOUR")

public class UserPostBehaviour implements Serializable {

    @EmbeddedId
    private UserPostBehaviourKey key = new UserPostBehaviourKey();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @MapsId("userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    @MapsId("postId")
    private Post post;

    private Boolean userHasSeen;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;
}
