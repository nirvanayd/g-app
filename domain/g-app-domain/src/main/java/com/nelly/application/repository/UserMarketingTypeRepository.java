package com.nelly.application.repository;


import com.nelly.application.domain.UserMarketing;
import com.nelly.application.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMarketingTypeRepository extends JpaRepository<UserMarketing, Long> {
    List<UserMarketing> findAllByUser(Users user);
}
