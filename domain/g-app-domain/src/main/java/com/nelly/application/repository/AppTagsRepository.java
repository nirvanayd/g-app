package com.nelly.application.repository;

import com.nelly.application.domain.AppTags;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppTagsRepository extends JpaRepository<AppTags, Long> {
    Optional<AppTags> findFirstByTag(String tag);
    Page<AppTags> findAllByTagContains(String tag, Pageable pageable);
}
