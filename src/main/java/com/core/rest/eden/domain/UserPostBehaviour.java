package com.core.rest.eden.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "USER_POST_BEHAVIOUR")
public class UserPostBehaviour implements Serializable {

    @EmbeddedId
    private UserPostBehaviourKey key;

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
