package com.nelly.application.repository;

import com.nelly.application.domain.ContentImages;
import com.nelly.application.domain.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentImagesRepository extends JpaRepository<ContentImages, Long> {
    void deleteAllByContent(Contents contents);
}
