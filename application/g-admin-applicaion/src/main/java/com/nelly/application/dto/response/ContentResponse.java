package com.nelly.application.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nelly.application.domain.ContentImages;
import com.nelly.application.domain.Users;
import com.nelly.application.enums.BrandStatus;
import lombok.Data;

import java.util.List;

@Data
public class ContentResponse {
    private long id;
    private String contentText;
    @JsonIgnore
    private Users user;
    @JsonIgnore
    private List<ContentImages> contentImages;

    private String loginId;
    private String thumbnail;
    private Integer likeCount;
    private Integer markCount;
    private Integer replyCount;
    private Integer viewCount;

    public void setUser(Users user) {
        this.user = user;
        this.loginId = user.getLoginId();
    }

    public void setContentImages(List<ContentImages> contentImages) {
        this.contentImages = contentImages;
        if (contentImages.size() > 0) {
            this.thumbnail = contentImages.get(0).getContentImageUrl();
        }
    }
}
