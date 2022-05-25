package com.core.rest.eden.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data

@Entity
@Table(name = "FRIENDSHIP_STATUS")

@SequenceGenerator(name = "idGenerator", sequenceName = "FRIENDSHIP_STATUS_SEQ", allocationSize = 1)
public class FriendshipStatus extends BaseModel {

    @NotNull(message = "{requester_id.null}")
    @OneToOne
    private User requester;

    @NotNull(message = "{addressee_id.null}")
    @OneToOne
    private User addressee;

    @NotNull(message = "{insertion_timestamp.null}")
    @Column(name = "insertion_timestamp", nullable = false)
    private LocalDateTime insertionTimestamp;

    @NotNull(message = "{specifier_id.null}")
    @OneToOne
    private User specifier;

    @NotNull(message = "{friendship_status.null}")
    @Column(name = "friendship_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status friendshipStatus;
}
