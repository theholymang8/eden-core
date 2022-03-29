package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
