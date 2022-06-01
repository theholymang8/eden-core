package com.core.rest.eden.transfer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class FileDTO {

    private String base64;

    private String name;

    private Long size;

    private Long lastModified;

    private String contentType;

}
