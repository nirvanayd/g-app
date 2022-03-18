package com.nelly.application.service.content;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.GetContentListRequest;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {
    private final ContentDomainService contentDomainService;
    private final UserService userService;

    public Page<Contents> getContentList(GetContentListRequest requestDto) {
        System.out.println(requestDto);
        List<Users> userList =  (requestDto.getLoginId() == null) ? null : userService.getAccountList(requestDto.getLoginId());
        YesOrNoType isDeleted = (requestDto.getIsDeleted() == null) ? null : YesOrNoType.getYesOrNoType(requestDto.getIsDeleted());

        return contentDomainService.selectContentList(requestDto.getPage(), requestDto.getSize(),
                userList, requestDto.getIsDeleted());
    }

    public Contents getContent(Long contentId) {
        return contentDomainService.selectContent(contentId)
                .orElseThrow(() -> new RuntimeException("컨텐츠 정보를 조회할 수 없습니다."));
    }

}
