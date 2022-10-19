package com.nelly.application.repository;

import com.nelly.application.domain.UserFollow;
import com.nelly.application.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    Optional<UserFollow> findByUserAndFollower(Users user, Users follower);
    Page<UserFollow> findAllByFollower(Users user, Pageable pageable);
    Page<UserFollow> findAllByUser(Users user, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.followerCount = u.followerCount + :value WHERE u.id = :userId")
    void updateFollowerCount(@Param("userId") Long contentId, @Param("value") int value);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.followingCount = u.followingCount+ :value WHERE u.id = :userId")
    void updateFollowingCount(@Param("userId") Long contentId, @Param("value") int value);
}
