package com.nelly.application.repository;


import com.nelly.application.domain.UserStyles;
import com.nelly.application.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStylesRepository extends JpaRepository<UserStyles, Long> {
    List<UserStyles> findAllByUser(Users user);
}
