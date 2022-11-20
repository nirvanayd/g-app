package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.enums.*;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDomainService {

    private final AppUserRepository userRepository;
    private final UserStylesRepository userStylesRepository;
    private final UserMarketingTypeRepository userMarketingTypeRepository;
    private final UserAgreementsRepository userAgreementsRepository;
    private final UserNotificationTokensRepository userNotificationTokensRepository;
    private final UserFollowRepository userFollowRepository;

    public Users addUser(Long authId, String loginId, String email, String birth, Authority authority) {
        Users user = Users.builder().authId(authId)
                .loginId(loginId)
                .role(authority.name())
                .email(email)
                .birth(birth)
                .status(UserStatus.NORMAL)
                .joinedDate(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public void addUserStyle(Users user, List<String> userStyle) {
        Users refUser = userRepository.getById(user.getId());
        for (String code: userStyle) {
            UserStyles userStyles = UserStyles.builder().styleType(StyleType.getStyleType(code)).user(refUser).build();
            userStylesRepository.save(userStyles);
        }
    }

    public void addUserMarketingType(Users user, List<String> userMarketingType) {
        Users refUser = userRepository.getById(user.getId());
        for (String code: userMarketingType) {
            UserMarketing userMarketing = UserMarketing.builder().marketingType(MarketingType.getMarketingType(code)).user(refUser).build();
            userMarketingTypeRepository.save(userMarketing);
        }
    }

    public Users getUsers(long authId) {
        Users user = userRepository.findByAuthId(authId).orElse(null);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");
        return user;
    }

    public Optional<Users> selectAppUsers(long authId) {
        return userRepository.findByAuthId(authId);
    }

    public Users selectAccount(long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new RuntimeException("사용자를 찾을 수 없습니다.");
        return user;
    }

    public Page<Users> selectAccountList(String keyword, String role, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("loginId").ascending());
        return userRepository.findAllByLoginIdContainsAndRole(keyword, role, pageRequest);
    }

    /* admin 사용 */
    public Page<Users> selectAccountList(Integer page, Integer size, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userRepository.findAllByRole(role, pageRequest);
    }

    public Page<Users> selectAccountList(Integer page, Integer size, String email, UserStatus status, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (email == null) return userRepository.findAllByStatusAndRole(status, role, pageRequest);
        if (status == null) return userRepository.findAllByEmailContainsAndRole(email, role, pageRequest);
        return userRepository.findAllByEmailContainsAndStatusAndRole(email, status, role, pageRequest);
    }

    public Page<Users> selectAccountList(Integer page, Integer size, String loginId, String email, UserStatus status, String role) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        if (loginId == null && email == null && status == null) return selectAccountList(page, size, role);
        if (loginId == null) return selectAccountList(page, size, email, status, role);
        if (email == null && status == null) {
            return userRepository.findAllByLoginIdContainsAndRole(loginId, role, pageRequest);
        }
        if (status == null) return userRepository.findAllByLoginIdContainsAndEmailContainsAndRole(loginId, email, role, pageRequest);
        if (email == null) return userRepository.findAllByLoginIdContainsAndStatusAndRole(loginId, status, role, pageRequest);
        return userRepository.findAllByLoginIdContainsAndEmailContainsAndStatusAndRole(loginId, email, status, role, pageRequest);
    }


    public boolean existEmail(String email) {
        Optional<Users> user = userRepository.findByEmailAndRole(email, RoleType.USER.getCode());
        return user.isPresent();
    }

    public List<Users> selectAccountListByLoginId(String loginId) {
        return userRepository.findAllByLoginIdContainsAndRole(loginId, RoleType.USER.getCode());
    }

    public Optional<Users> selectAccountByLoginId(String loginId) {
        return userRepository.findByLoginIdAndRole(loginId, RoleType.USER.getCode());
    }

    public Optional<Users> selectAccountByEmail(String email) {
        return userRepository.findByEmailAndRole(email, RoleType.USER.getCode());
    }

    public Users saveAccountEmail(Users user, String email) {
        user.setEmail(email);
        return userRepository.save(user);
    }

    public List<UserAgreements> getUserAgreements(Users user) {
        return userAgreementsRepository.findAllByUserId(user.getId());
    }

    public void addUserAgreement(Users user, String agreementType, String value) {
        Users refUser = userRepository.getById(user.getId());
        UserAgreements userAgreement = UserAgreements.builder()
                .userId(refUser.getId())
                .useYn(value)
                .agreementType(agreementType)
                .build();
        userAgreementsRepository.save(userAgreement);
    }

    public void saveUserAgreement(Long userId, String agreementType, String value) {
        Optional<UserAgreements> userAgreements = userAgreementsRepository.findByUserIdAndAgreementType(userId, agreementType);
        if (userAgreements.isEmpty()) return;
        UserAgreements refUserAgreement = userAgreementsRepository.findById(userAgreements.get().getId()).orElse(null);
        if (refUserAgreement == null) return;
        if (value == null) return;
        refUserAgreement.setUseYn(value);
        userAgreementsRepository.save(refUserAgreement);
    }

    public Optional<UserNotificationTokens> existFcmToken(Users user) {
        return userNotificationTokensRepository.findByUser(user);
    }

    public UserNotificationTokens saveUserToken(UserNotificationTokens userNotificationToken, String token) {
        UserNotificationTokens existNotificationToken = userNotificationTokensRepository.getById(userNotificationToken.getId());
        existNotificationToken.setFcmToken(token);
        return userNotificationTokensRepository.save(existNotificationToken);
    }

    public UserNotificationTokens saveUserToken(Users user, String token) {
        UserNotificationTokens userNotificationToken = UserNotificationTokens.builder()
                .user(user)
                .fcmToken(token)
                .build();
        return userNotificationTokensRepository.save(userNotificationToken);
    }

    public void saveUserStyles(Users user, List<String> styleList) {
        List<UserStyles> existStylesList = userStylesRepository.findAllByUser(user);
        userStylesRepository.deleteAll(existStylesList);

        for (String code: styleList) {
            UserStyles userStyles = UserStyles.builder().styleType(StyleType.getStyleType(code)).user(user).build();
            userStylesRepository.save(userStyles);
        }
    }

    public Optional<UserFollow> selectUserFollow(Users user, Users followingUser) {
        return userFollowRepository.findByUserAndFollower(user, followingUser);
    }

    public void saveUserFollow(Users user, Users follower) {
        UserFollow userFollow = UserFollow.builder().
                user(user).
                follower(follower).
                build();
        userFollowRepository.save(userFollow);
    }

    public void deleteUserFollow(long id) {
        userFollowRepository.deleteById(id);
    }

    public void updateUserFollowerCount(Long userId, Integer count) {
        userFollowRepository.updateFollowerCount(userId, count);
    }

    public void updateUserFollowingCount(Long userId, Integer count) {
        userFollowRepository.updateFollowingCount(userId, count);
    }

    public void saveAccountProfileTitle(Users user, String profileTitle) {
        user.setProfileTitle(profileTitle);
        userRepository.save(user);
    }

    public void saveAccountProfileText(Users user, String profileText) {
        user.setProfileText(profileText);
        userRepository.save(user);
    }

    public void saveAccountProfileImage(Users user, String imageUrl) {
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

    public void saveAccountBackgroundImage(Users user, String imageUrl) {
        user.setBackgroundImageUrl(imageUrl);
        userRepository.save(user);
    }

    public Page<UserFollow> selectAccountFollowerList(Users user, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userFollowRepository.findAllByFollower(user, pageRequest);
    }

    public Page<UserFollow> selectAccountFollowerList(Users user, String keyword, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userFollowRepository.findAllByFollowerAndUser_LoginIdContains(user, keyword, pageRequest);
    }

    public Page<UserFollow> selectAccountFollowingList(Users user, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userFollowRepository.findAllByUser(user, pageRequest);
    }

    public Page<UserFollow> selectAccountFollowingList(Users user, String keyword, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return userFollowRepository.findAllByUserAndFollower_LoginIdContains(user, keyword, pageRequest);
    }

    public void deleteUserFcmToken(UserNotificationTokens fcmToken) {
        userNotificationTokensRepository.delete(fcmToken);
    }

    public List<Users> selectBlockUserList() {
        return userRepository.findAllByStatus(UserStatus.BLOCK);
    }

    public List<Users> selectLeaveUserList() {
        return userRepository.findAllByStatus(UserStatus.LEAVE);
    }
}