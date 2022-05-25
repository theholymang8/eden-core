package com.core.rest.eden.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuperBuilder

@Entity
@Table(name = "FRIENDSHIP")

@SequenceGenerator(name = "idGenerator", sequenceName = "FRIENDSHIP_SEQ", allocationSize = 1)
public class Friendship extends BaseModel {

    @NotNull(message = "{requester_id.null}")
    @OneToOne
    private User requester;

    @NotNull(message = "{addressee_id.null}")
    @OneToOne
    private User addressee;

    @NotNull(message = "{created_at.null}")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
