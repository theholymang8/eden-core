package com.core.rest.eden.services;

import com.core.rest.eden.domain.File;
import com.core.rest.eden.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class FileServiceImpl extends BaseServiceImpl<File> implements FileService{

    private final FileRepository fileRepository;

    @Override
    public JpaRepository<File, Long> getRepository() {
        return fileRepository;
    }

    @Override
    public void save(MultipartFile file) throws IOException {
        File fileEntity = File.builder()
                .name(StringUtils.cleanPath(file.getOriginalFilename()))
                .contentType(file.getContentType())
                .data(file.getBytes())
                .build();

        fileRepository.save(fileEntity);
    }

    @Override
    public File findByName(String name) {
        return fileRepository.findByName(name);
    }
}
