package com.nelly.application.service.search;

import com.nelly.application.domain.*;
import com.nelly.application.dto.request.SearchRequest;
import com.nelly.application.dto.request.SearchTagContentRequest;
import com.nelly.application.dto.response.*;
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
    
    public List<SearchTagResponse> searchTagList(SearchRequest dto) {
        // 태그 정보
        Page<AppTags> selectAppTagList =
                contentDomainService.getAppTagList(dto.getKeyword(), dto.getPage(), dto.getSize());

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

        List<String> currentList = new ArrayList<>();
        currentList.add("test1");
        currentList.add("test22");
        currentList.add("tester22");
        currentList.add("PEARLY GATES");
        currentList.add("ST ANDREWS");
        currentList.add("DUVETICA");
        currentList.add("MASTER");
        currentList.add("MASTER BUNNY");
        currentList.add("PING");
        currentList.add("까스텔바작");


        List<String> brandList = new ArrayList<>();
        brandList.add("DUVETICA");
        brandList.add("CASTELBAJAC");
        brandList.add("MASTER BUNNY EDITION");
        brandList.add("PEARLY GATES");
        brandList.add("ST ANDREWS");
        brandList.add("PING");

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
}
