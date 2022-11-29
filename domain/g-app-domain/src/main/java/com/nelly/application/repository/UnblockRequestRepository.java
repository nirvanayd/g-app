package com.nelly.application.repository;


import com.nelly.application.domain.Contents;
import com.nelly.application.domain.UnblockRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnblockRequestRepository extends JpaRepository<UnblockRequest, Long> {
    Optional<UnblockRequest> findByContentAndStatus(Contents content,int status);
}
