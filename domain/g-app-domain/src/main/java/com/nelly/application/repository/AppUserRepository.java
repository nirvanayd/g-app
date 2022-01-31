package com.nelly.application.repository;


import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByAuthId(long authId);
    Page<Users> findAllByRole(String role, Pageable pageable);
}
