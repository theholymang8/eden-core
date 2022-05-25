package com.core.rest.eden.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder

@Entity
@Table(name = "FILES")

@SequenceGenerator(name = "idGenerator", sequenceName = "FILE_SEQ", allocationSize = 1)
public class File extends BaseModel {

    private String name;

    private String contentType;

    private Long size;

    @JsonIgnore
    @Lob
    private byte[] data;

    private String url;

    @ManyToOne
    private User user;

    @ManyToOne
    private Post post;

}
