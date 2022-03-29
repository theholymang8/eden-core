package com.core.rest.eden.repositories;

import com.core.rest.eden.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
