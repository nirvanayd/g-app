package com.nelly.application.repository;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.ReportContents;
import com.nelly.application.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportContentsRepository extends JpaRepository<ReportContents, Long> {
    Optional<ReportContents> findByContentAndAndUser(Contents content, Users user);
}
