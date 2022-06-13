package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Post;

import java.util.Optional;

public interface CustomPostRepository {

    Post findByIdCustom(Long id);
}
