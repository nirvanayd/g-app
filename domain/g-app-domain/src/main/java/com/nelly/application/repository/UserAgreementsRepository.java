package com.nelly.application.repository;

import com.nelly.application.domain.Agreements;
import com.nelly.application.domain.UserAgreements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAgreementsRepository extends JpaRepository<UserAgreements, Long> {
    List<UserAgreements> findAllByUserId(Long userId);
}
