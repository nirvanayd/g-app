package com.nelly.application.repository;


import com.nelly.application.domain.Users;
import com.nelly.application.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByAuthId(long authId);
    Page<Users> findAllByRole(String role, Pageable pageable);

    Page<Users> findAllByLoginIdContainsAndRole(String loginId, String role, Pageable pageable);
    Page<Users> findAllByEmailContainsAndRole(String email, String role, Pageable pageable);
    Page<Users> findAllByStatusAndRole(UserStatus status, String role, Pageable pageable);

    Page<Users> findAllByLoginIdContainsAndEmailContainsAndRole(String loginId, String email, String role, Pageable pageable);
    Page<Users> findAllByLoginIdContainsAndStatusAndRole(String loginId, UserStatus status, String role, Pageable pageable);
    Page<Users> findAllByEmailContainsAndStatusAndRole(String email, UserStatus status, String role, Pageable pageable);

    Page<Users> findAllByLoginIdContainsAndEmailContainsAndStatusAndRole(String loginId, String email, UserStatus status,
                                                                         String role, Pageable pageable);

    List<Users> findAllByLoginIdContainsAndRole(String loginId, String role);
    Optional<Users> findByLoginIdAndRole(String loginId, String role);
    Optional<Users> findByEmailAndRole(String email, String role);

    List<Users> findAllByStatus(UserStatus status);
    Optional<Users> findByIdAndRole(long userId, String role);
}
