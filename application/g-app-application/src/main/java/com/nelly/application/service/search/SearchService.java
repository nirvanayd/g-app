package com.nelly.application.service.search;

import com.nelly.application.domain.*;
import com.nelly.application.dto.request.RemoveCurrentKeywordRequest;
import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.dto.request.SearchTagContentRequest;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.RoleType;
import com.nelly.application.enums.SearchLogType;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.SearchDomainService;
import com.nelly.application.service.UserDomainService;
import com.nelly.application.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final UserDomainService userDomainService;
    private final BrandDomainService brandDomainService;
    private final ContentDomainService contentDomainService;
    private final SearchDomainService searchDomainService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public List<AccountResponse> searchAccount(SearchRequest dto) {
        Optional<Users> user = userService.getAppUser();
        searchDomainService.saveLog(user, SearchLogType.BRAND, dto.getKeyword());
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
        searchDomainService.saveLog(user, SearchLogType.BRAND, dto.getKeyword());

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
    
    public List<SearchTagResponse> searchTagList(SearchRequest dto) {
        // 태그 정보
        Page<AppTags> selectAppTagList =
                contentDomainService.getAppTagList(dto.getKeyword(), dto.getPage(), dto.getSize());

        Optional<Users> user = userService.getAppUser();
        searchDomainService.saveLog(user, SearchLogType.TAG, dto.getKeyword());

        if (selectAppTagList.isEmpty()) throw new NoContentException();

        List<AppTags> appTagList = selectAppTagList.getContent();

        return appTagList.stream().
                map(u -> modelMapper.map(u, SearchTagResponse.class)).collect(Collectors.toList());
    }

    public List<ContentThumbResponse> searchContentList(SearchTagContentRequest dto) {
        Page<Contents> selectContentList = contentDomainService.selectAppTagContentList(dto.getId(), dto.getPage(), dto.getSize());
        if (selectContentList.isEmpty()) throw new NoContentException();
        ContentThumbResponse response = new ContentThumbResponse();
        return response.toDtoList(selectContentList.getContent());
    }

    public GetSearchIntroResponse getSearchIntroData() {
        GetSearchIntroResponse getSearchIntroResponse = new GetSearchIntroResponse();

        Optional<Users> user = userService.getAppUser();

        List<String> currentList = new ArrayList<>();
        if (user.isPresent()) {
            Page<SearchLog> searchLogList
                    = searchDomainService.selectUserSearchLog(user.get());

            searchLogList.stream().forEach(l -> {
                currentList.add(l.getKeyword());
            });
        }

        List<SearchBrandResponse> brandList = new ArrayList<>();

        Page<Brands> selectBrandList = brandDomainService.selectBrandList(0, 10);

        selectBrandList.stream().forEach(l -> {
            brandList.add(SearchBrandResponse.builder().id(l.getId()).photoURL(l.getLogoImageUrl()).name(l.getName()).build());
        });


        List<String> hotKeywordList = new ArrayList<>();
        hotKeywordList.add("듀베티카");
        hotKeywordList.add("필드코디");
        hotKeywordList.add("까스텔바작");
        hotKeywordList.add("핑");
        hotKeywordList.add("tester12");
        hotKeywordList.add("test1");
        hotKeywordList.add("hello");
        hotKeywordList.add("헬로우");

        getSearchIntroResponse.setHotBrandList(brandList);
        getSearchIntroResponse.setHotKeywordList(hotKeywordList);
        getSearchIntroResponse.setCurrentKeywordList(currentList);

        return getSearchIntroResponse;
    }

    public void removeCurrentKeyword(RemoveCurrentKeywordRequest dto) {
        Optional<Users> selectUser = userService.getAppUser();
        if (selectUser.isEmpty()) throw new SystemException("사용자 정보를 조회할 수 없습니다.");
        searchDomainService.deleteUserCurrentKeyword(selectUser.get(), dto.getKeyword());
        return;
    }
}