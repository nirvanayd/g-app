package com.nelly.application.repository;

import com.nelly.application.domain.ContentHashTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentHashTagsRepository extends JpaRepository<ContentHashTags, Long> {
}
