package com.nelly.application.service.content;

import com.nelly.application.domain.Brands;
import com.nelly.application.domain.Contents;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.GetBrandListRequest;
import com.nelly.application.dto.request.GetContentListRequest;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.service.BrandDomainService;
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

        System.out.println(userList);
        return null;
    }
}
