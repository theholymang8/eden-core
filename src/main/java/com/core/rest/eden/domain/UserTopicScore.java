package com.core.rest.eden.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder

@Entity
@Table(name = "USER_TOPIC_SCORES")

@SequenceGenerator(name = "idGenerator", sequenceName = "USER_TOPIC_SCORE_SEQ", allocationSize = 1)
public class UserTopicScore extends BaseModel{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Float topicScore;

    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

}
