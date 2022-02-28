package com.nelly.application.repository;

import com.nelly.application.domain.UserHashTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHashTagsRepository extends JpaRepository<UserHashTags, Long> {

}
