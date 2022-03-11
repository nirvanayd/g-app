package com.nelly.application.repository;

import com.nelly.application.domain.AppTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppTagsRepository extends JpaRepository<AppTags, Long> {
}
