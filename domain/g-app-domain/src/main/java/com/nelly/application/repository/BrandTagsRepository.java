package com.nelly.application.repository;

import com.nelly.application.domain.BrandTags;
import com.nelly.application.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BrandTagsRepository extends JpaRepository<BrandTags, Long> {
    void deleteAllByContent(Contents contents);
}
