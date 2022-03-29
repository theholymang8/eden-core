package com.core.rest.eden.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {})
@EqualsAndHashCode(callSuper = true, exclude = {})
@Data

@Entity
@Table(name = "GROUPS")

@SequenceGenerator(name = "idGenerator", sequenceName = "GROUP_SEQ", allocationSize = 1)
public class Group extends BaseModel{

    @NotNull(message = "{title.null}")
    @Column(length = 200, nullable = false)
    private String title;

    @NotNull(message = "{about.null}")
    @Column(length = 6000, nullable = false)
    private String about;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY)
    @JoinTable(name = "`USER_GROUPS`",
            joinColumns = @JoinColumn(name = "`user_id`"),
            foreignKey = @ForeignKey(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "`group_id`"),
            inverseForeignKey = @ForeignKey(name = "group_id")
    )
    @JsonIgnoreProperties(value = "users")
    private Set<User> members = new HashSet<>();

}
