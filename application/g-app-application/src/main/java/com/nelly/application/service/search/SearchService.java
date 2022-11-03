package com.nelly.application.service.search;

import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.dto.response.AccountResponse;
import com.nelly.application.dto.response.ContentMemberResponse;
import com.nelly.application.enums.RoleType;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final UserDomainService userDomainService;

    public List<AccountResponse> searchAccount(SearchRequest dto) {
        Page<Users> selectAccountList =
                userDomainService.selectAccountList(dto.getKeyword(), RoleType.USER.getCode(), dto.getPage(), dto.getSize());

        if (selectAccountList.isEmpty()) throw new NoContentException();

        List<Users> accountList = selectAccountList.getContent();

        AccountResponse accountResponse = new AccountResponse();
        return accountResponse.toDtoList(accountList);
    }
}
