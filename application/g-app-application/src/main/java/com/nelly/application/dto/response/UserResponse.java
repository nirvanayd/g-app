package com.nelly.application.dto.response;

import com.nelly.application.domain.UserAgreements;
import com.nelly.application.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse {
    private long id;
    private String loginId;
    private String email;
    private String birth;
    private String profileImageUrl;
    private String profileTitle;
    private String profileText;
    private UserStatus status;
    private String statusCode;
    private String statusDesc;
    private Integer followingCount;
    private Integer followerCount;
    private LocalDateTime joinedDate;
    private LocalDateTime leaveDate;
    private List<UserStylesResponse> userStyles;
    private List<UserAgreementsResponse> userAgreements;

    public void setStatus(UserStatus userStatus) {
        this.status = userStatus;
        this.statusCode = userStatus.getCode();
        this.statusDesc = userStatus.getDesc();
    }
}
