package com.nelly.application.repository;


import com.nelly.application.domain.UserMarketing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMarketingTypeRepository extends JpaRepository<UserMarketing, Long> {
}
