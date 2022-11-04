package com.nelly.application.service.search;

import com.nelly.application.domain.AppTags;
import com.nelly.application.domain.Brands;
import com.nelly.application.domain.UserBrands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.dto.response.AccountResponse;
import com.nelly.application.dto.response.BrandRankResponse;
import com.nelly.application.dto.response.ContentMemberResponse;
import com.nelly.application.dto.response.SearchTagResponse;
import com.nelly.application.enums.RoleType;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final UserDomainService userDomainService;
    private final BrandDomainService brandDomainService;
    private final ContentDomainService contentDomainService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<AccountResponse> searchAccount(SearchRequest dto) {
        Page<Users> selectAccountList =
                userDomainService.selectAccountList(dto.getKeyword(), RoleType.USER.getCode(), dto.getPage(), dto.getSize());

        if (selectAccountList.isEmpty()) throw new NoContentException();

        List<Users> accountList = selectAccountList.getContent();

        AccountResponse accountResponse = new AccountResponse();
        return accountResponse.toDtoList(accountList);
    }

    public List<BrandRankResponse> searchBrandList(SearchRequest dto) {
        Page<Brands> selectBrandList =
                brandDomainService.selectBrandListByKeyword(dto.getKeyword(), dto.getPage(), dto.getSize());

        if (selectBrandList.isEmpty()) throw new NoContentException();
        Optional<Users> user = userService.getAppUser();

        List<Brands> brandList = selectBrandList.getContent();

        List<BrandRankResponse> list = brandList.stream().
                map(u -> modelMapper.map(u, BrandRankResponse.class)).collect(Collectors.toList());

        List<Long> brandIdList = list.stream().map(BrandRankResponse::getId).collect(Collectors.toList());

        if (user.isPresent()) {
            List<UserBrands> userBrandList = brandDomainService.selectAppUserBrandList(user.get().getId(), brandIdList);
            for (BrandRankResponse brandRankResponse : list) {
                boolean isFavorite = userBrandList.stream().anyMatch(u -> u.getBrand().getId().equals(brandRankResponse.getId()));
                brandRankResponse.setIsFavorite(isFavorite);
                //
            }
        }
        return list;
    }
    
    public List<SearchTagResponse> searchContentList(SearchRequest dto) {
        // 태그 정보
        Page<AppTags> selectAppTagList =
                contentDomainService.getAppTagList(dto.getKeyword(), dto.getPage(), dto.getSize());

        if (selectAppTagList.isEmpty()) throw new NoContentException();

        List<AppTags> appTagList = selectAppTagList.getContent();

        return appTagList.stream().
                map(u -> modelMapper.map(u, SearchTagResponse.class)).collect(Collectors.toList());
    }
}
