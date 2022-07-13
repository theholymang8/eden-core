package com.core.rest.eden.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "USER_AURAS")

@SequenceGenerator(name = "idGenerator", sequenceName = "AURA_SEQ", allocationSize = 1)
public class Aura extends BaseModel{

    //private Float
    private Float score;

}
