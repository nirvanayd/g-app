package com.nelly.application.repository;


import com.nelly.application.domain.UserNotificationTokens;
import com.nelly.application.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserNotificationTokensRepository extends JpaRepository<UserNotificationTokens, Long> {
    Optional<UserNotificationTokens> findByUser(Users user);
}
