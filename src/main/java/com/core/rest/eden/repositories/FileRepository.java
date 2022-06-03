package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File findByName(String name);
}
