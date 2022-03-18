package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.domain.BrandHashTags;
import com.nelly.application.domain.ContentImages;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.YesOrNoType;
import lombok.Data;

import java.util.List;

@Data
public class ContentDetailResponse {
    private long id;
    private String contentText;
    @JsonIgnore
    private Users user;
    private List<ContentImageResponse> contentImages;
    private String loginId;
    private Integer likeCount;
    private Integer markCount;
    private Integer replyCount;
    private Integer viewCount;

    @JsonIgnore
    private YesOrNoType deletedYn;

    private String deletedYnDesc;
    private String deletedYnCode;

    public void setDeletedYn(YesOrNoType deletedYn) {
        this.deletedYn = deletedYn;
        this.deletedYnCode = deletedYn.getCode();
        this.deletedYnDesc = deletedYn.getDesc();
    }

    public void setUser(Users user) {
        this.user = user;
        this.loginId = user.getLoginId();
    }
}
