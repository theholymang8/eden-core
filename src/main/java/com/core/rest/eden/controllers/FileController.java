package com.core.rest.eden.controllers;

import com.core.rest.eden.controllers.transfer.ApiResponse;
import com.core.rest.eden.domain.File;
import com.core.rest.eden.domain.Post;
import com.core.rest.eden.services.BaseService;
import com.core.rest.eden.services.FileService;
import lombok.RequiredArgsConstructor;
import org.hibernate.hql.internal.ast.util.ASTParentsFirstIterator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController extends AbstractController<File> {

    private final FileService fileService;

    @Override
    protected BaseService<File, Long> getBaseService() {
        return fileService;
    }

    @PostMapping(headers = "action=uploadFile")
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file")MultipartFile file) {
        try {
            logger.info("File is: {}", file);
            fileService.save(file);
            return new ResponseEntity<>(ApiResponse.<String>builder().data(String.format("File uploaded successfully: %s", file.getOriginalFilename())).build(),
                    getNoCacheHeaders(), HttpStatus.OK);
        }catch (IOException ioException){
            return new ResponseEntity<>(ApiResponse.<String>builder().data(String.format("Could not upload the file: %s!", file.getOriginalFilename())).build(),
                    getNoCacheHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(headers = "action=listFiles")
    public ResponseEntity<ApiResponse<List<File>>> list() {
        List<File> foundFiles = fileService.findAll()
                .stream()
                .map(this::mapToFileResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.<List<File>>builder().data(foundFiles).build());

    }

    private File mapToFileResponse(File fileEntity) {
        String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(fileEntity.getId().toString())
                .toUriString();
        return File.builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .contentType(fileEntity.getContentType())
                .size(fileEntity.getSize())
                .url(downloadURL)
                .build();
    }

    @GetMapping(value = "/{id}",
    headers = "action=downloadFile")
    private ResponseEntity<ApiResponse<String>> getFile(@PathVariable Long id) throws NoSuchElementException {
        File fileOptional = fileService.find(id);

        if (fileOptional == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ApiResponse.<String>builder().data(fileOptional.getData()).build());
    }
}
