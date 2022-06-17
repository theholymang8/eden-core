package com.core.rest.eden.domain;

import com.core.rest.eden.transfer.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"user", "userAvatar", "post"})
@EqualsAndHashCode(callSuper = true, exclude = {"user", "userAvatar", "post"})
@Data
@SuperBuilder

@Entity
@Table(name = "FILES")

@JsonIgnoreProperties(value = {"userAvatar", "user", "post"})

@SequenceGenerator(name = "idGenerator", sequenceName = "FILE_SEQ", allocationSize = 1)
public class File extends BaseModel {

    @JsonView(Views.Public.class)
    private String name;

    @JsonView(Views.Public.class)
    private String contentType;

    @JsonView(Views.Public.class)
    private Long size;

    @Lob
    @JsonView(Views.Public.class)
    @Getter(AccessLevel.NONE)
    /*@Type(type = "org.hibernate.type.ImageType")*/
    //@Type(type = "org.hibernate.type.PrimitiveByteArrayBlobType")
    private byte[] data;

    @JsonView(Views.Public.class)
    private String url;

    @ManyToOne
    @JsonView(Views.Internal.class)
    private User user;

    @OneToOne
    @JsonView(Views.Detailed.class)
    private User userAvatar;

    //@MapsId
    @OneToOne
    @JoinColumn(name = "post_id")
    @JsonView(Views.Internal.class)
    private Post post;


    public String getData() {
        String encodedImage = Base64.getEncoder().encodeToString(this.data);
        return encodedImage;
        //return "";
    }

}
