package com.nelly.application.repository;


import com.nelly.application.domain.UserStyles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStylesRepository extends JpaRepository<UserStyles, Long> {
}
