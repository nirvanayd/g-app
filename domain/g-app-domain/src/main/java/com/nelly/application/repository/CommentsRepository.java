package com.nelly.application.repository;

import com.nelly.application.domain.Comments;
import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    Optional<Comments> findById(Long id);
    Optional<Comments> findByIdAndUser(Long id, Users user);
    Page<Comments> findAllByContentAndParentNull(Contents contents , Pageable pageable);
    Page<Comments> findAllByParent(Comments comment, Pageable pageable);
}
