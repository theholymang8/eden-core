package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"user", "userAvatar", "post"})
@EqualsAndHashCode(callSuper = true, exclude = {"user", "userAvatar", "post"})
@Data
@SuperBuilder

@Entity
@Table(name = "FILES")

//@JsonIgnoreProperties(value = {"groups", "user", "comment"})

@SequenceGenerator(name = "idGenerator", sequenceName = "FILE_SEQ", allocationSize = 1)
public class File extends BaseModel {

    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Public.class)
    private String contentType;

    @JsonView(Views.Public.class)
    private Long size;

    @JsonIgnore
    @Lob
    @JsonView(Views.Internal.class)
    /*@Type(type = "org.hibernate.type.ImageType")*/
    //@Type(type = "org.hibernate.type.PrimitiveByteArrayBlobType")
    private byte[] data;

    @JsonView(Views.Public.class)
    private String url;

    @ManyToOne
    @JsonView(Views.Internal.class)
    private User user;

    @OneToOne
    @JsonView(Views.Internal.class)
    private User userAvatar;

    @ManyToOne
    @JsonView(Views.Internal.class)
    private Post post;

}
