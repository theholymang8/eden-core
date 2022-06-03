package com.core.rest.eden.services;

import com.core.rest.eden.domain.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService extends BaseService<File, Long> {

    void save(MultipartFile file) throws IOException;

    File findByName(String name);

}
