package com.nelly.application.repository;

import com.nelly.application.domain.AppAuthentication;
import com.nelly.application.enums.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AppAuthentication, Long> {
    Optional<AppAuthentication> findByLoginId(String loginId);
    Optional<AppAuthentication> findByLoginIdAndRoles(String loginId, Authority authority);
}
