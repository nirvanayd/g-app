package com.nelly.application.repository;


import com.nelly.application.domain.SocialUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialUserRepository extends JpaRepository<SocialUsers, Long> {
    Optional<SocialUsers> findByUidAndType(String uid, String type);
    Optional<SocialUsers> findByAuthId(long authId);

}
