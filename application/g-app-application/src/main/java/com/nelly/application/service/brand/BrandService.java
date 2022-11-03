package com.nelly.application.service.brand;

import com.nelly.application.domain.*;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.*;
import com.nelly.application.dto.response.*;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.PlaceType;
import com.nelly.application.enums.StyleType;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.service.ContentDomainService;
import com.nelly.application.service.ScraperDomainService;
import com.nelly.application.service.scraper.ScraperService;
import com.nelly.application.service.user.UserService;
import com.nelly.application.util.CacheTemplate;
import com.nelly.application.util.ScraperManager;
import com.nelly.application.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BrandService {

    private final BrandDomainService brandDomainService;
    private final UserService userService;
    private final ScraperManager scraperManager;
    private final ScraperService scraperService;
    private final ScraperDomainService scraperDomainService;
    private final ModelMapper modelMapper;
    private final CacheTemplate cacheTemplate;
    private final ContentDomainService contentDomainService;

    public Brands getBrand(Long brandId) {
        return brandDomainService.selectBrand(brandId);
    }

    public BrandIntroResponse getBrandIntro(Long id, Users user) {
        Brands brand = getBrand(id);
        BrandIntroResponse brandIntroResponse = modelMapper.map(brand, BrandIntroResponse.class);

        List<String> list = new ArrayList<>();
        list.addAll(brand.getBrandStyles().stream().map(l -> l.getBrandStyle().getDesc()).collect(Collectors.toList()));
        list.addAll(brand.getBrandAges().stream().map(l -> l.getAgeType().getDesc()).collect(Collectors.toList()));
        list.addAll(brand.getBrandPlaces().stream().map(l -> l.getPlaceType().getDesc()).collect(Collectors.toList()));

        brandIntroResponse.setTagList(list);

        if (user != null) {
            Optional<UserBrands> userBrands = brandDomainService.selectAppUserBrand(user.getId(), brand);
            if (userBrands.isPresent()) brandIntroResponse.setIsFavorite(true);
        }
        return brandIntroResponse;
    }

    public void downloadImage() {
        String url = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/40b32ba35f6b41dc801dadcb0119de47_9366/Black_GZ3901_01_standard.jpg";
    }

    public GetRankResponse getBrandRankList(GetRankRequest getRankRequest) {
        PageRequest pageRequest = PageRequest.of(getRankRequest.getPage(), getRankRequest.getSize());
        // parameter 분기
        boolean adjustStyle = getRankRequest.getStyle() != null && getRankRequest.getStyle().size() > 0;
        boolean adjustPlace = getRankRequest.getPlace() != null && getRankRequest.getPlace().size() > 0;
        boolean adjustAge = getRankRequest.getAge() != null && getRankRequest.getAge().size() > 0;

        Page<BrandRank> rankPage = null;
        List<Brands> brandsList = null;
        List<BrandStyles> brandStyleList = null;
        List<BrandPlaces> brandPlaceList = null;
        List<BrandAges> brandAgeList = null;

        List<StyleType> styleTypeList = StyleType.getStyleList(getRankRequest.getStyle());
        List<AgeType> ageTypeList = AgeType.getAgeList(getRankRequest.getAge());
        List<PlaceType> placeTypeList = PlaceType.getPlaceList(getRankRequest.getPlace());

        brandStyleList = brandDomainService.selectBrandStyles(styleTypeList);
        brandAgeList = brandDomainService.selectBrandAges(ageTypeList);
        brandPlaceList = brandDomainService.selectBrandPlaces(placeTypeList);

        // make case
        if (adjustStyle && adjustPlace && adjustAge) {
            List<Brands> selectBrandList = brandDomainService.selectBrandListByStylePlaceAge(brandStyleList,
                    brandPlaceList, brandAgeList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        } else if (adjustStyle && adjustPlace) {
            List<Brands> selectBrandList = brandDomainService.selectBrandListByStylePlace(brandStyleList, brandPlaceList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        } else if (adjustPlace && adjustAge) {
//            List<Brands> selectBrandList = brandDomainService.selectBrandListByPlaceAge(brandPlaceList, brandAgeList);
//            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
//            return brandRankResponse(rankPage);
        } else if (adjustStyle && adjustAge) {
            List<Brands> selectBrandList = brandDomainService.selectBrandStylesAge(brandStyleList, brandAgeList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        } else if (adjustStyle) {
            List<Brands> selectBrandList = brandDomainService.selectBrandListByStyle(brandStyleList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        } else if (adjustPlace) {
            List<Brands> selectBrandList = brandDomainService.selectBrandListByPlace(brandPlaceList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        } else if (adjustAge) {
            List<Brands> selectBrandList = brandDomainService.selectBrandListByAge(brandAgeList);
            rankPage = brandDomainService.selectAppBrandRankList(selectBrandList, pageRequest);
            return brandRankResponse(rankPage);
        }
        rankPage = brandDomainService.selectAppBrandRankList(pageRequest);
        return brandRankResponse(rankPage);
    }

    private GetRankResponse brandRankResponse(Page<BrandRank> rankPage) {

        // list end 분기
        boolean isEnded = false;
        List<BrandRank> rankList = rankPage.getContent();
        List<BrandRankResponse> list = rankList.stream().
                map(u -> modelMapper.map(u.getBrand(), BrandRankResponse.class)).collect(Collectors.toList());

        List<Long> brandIdList = list.stream().map(BrandRankResponse::getId).collect(Collectors.toList());

        Optional<Users> user = userService.getAppUser();
        long totalCount = rankPage.getTotalElements();
        long totalPage = rankPage.getTotalPages();
        long contentSize = rankPage.getContent().size();

        if (totalPage == 0) {
            isEnded = true;
        }

        if (contentSize == 0) {
            isEnded = true;
        }

        if (user.isPresent()) {
            List<UserBrands> userBrandList = brandDomainService.selectAppUserBrandList(user.get().getId(), brandIdList);

            for (BrandRankResponse brandRankResponse : list) {

                boolean isFavorite = userBrandList.stream().anyMatch(u -> u.getBrand().getId().equals(brandRankResponse.getId()));

                brandRankResponse.setIsFavorite(isFavorite);
                //
            }
        }

        GetRankResponse getRankResponse = new GetRankResponse();
        getRankResponse.setTotalCount(totalCount);
        getRankResponse.setTotalPage(totalPage);
        getRankResponse.setBrandList(list);
        getRankResponse.setEnded(isEnded);
        return getRankResponse;
    }

    public void saveUserBrands(Long userId, SaveUserBrandsRequest saveUserBrandsRequest) {

        Brands brand = brandDomainService.selectBrand(saveUserBrandsRequest.getBrandId());
        Optional<UserBrands> userBrand = brandDomainService.selectUserBrand(saveUserBrandsRequest.getBrandId(), userId);

        if (YesOrNoType.YES.getCode().equals(saveUserBrandsRequest.getFavoriteYn())) {
            if (userBrand.isPresent()) return;
            brandDomainService.createUserBrand(brand, userId);
            cacheTemplate.incrValue(String.valueOf(saveUserBrandsRequest.getBrandId()), "favorite");
        } else if (YesOrNoType.NO.getCode().equals(saveUserBrandsRequest.getFavoriteYn())) {
            if (userBrand.isEmpty()) throw new SystemException("즐겨찾기 정보를 조회할 수 없습니다.");
            brandDomainService.deleteUserBrand(userBrand.get());
            cacheTemplate.decrValue(String.valueOf(saveUserBrandsRequest.getBrandId()), "favorite");
        }
    }

    public GetUserBrandsResponse getUserBrandList(Long userId, GetUserBrandsRequest getUserBrandsRequest) {
        // list ended
        boolean isEnded = false;
        Page<UserBrands> userBrandsPage = brandDomainService.selectUserBrandList(userId,
                getUserBrandsRequest.getPage(), getUserBrandsRequest.getSize());

        long totalCount = userBrandsPage.getTotalElements();
        long totalPage = userBrandsPage.getTotalPages();
        long contentSize = userBrandsPage.getContent().size();

        if (totalPage == 0) {
            isEnded = true;
        }
        if (contentSize == 0) {
            isEnded = true;
        }

        List<UserBrands> userBrandList = userBrandsPage.getContent();
        List<BrandFavoriteResponse> list = userBrandList.stream().
                map(u -> modelMapper.map(u.getBrand(), BrandFavoriteResponse.class)).collect(Collectors.toList());

        list.forEach(l -> l.setIsFavorite(true));

        GetUserBrandsResponse getUserBrandsResponse = new GetUserBrandsResponse();
        getUserBrandsResponse.setTotalCount(totalCount);
        getUserBrandsResponse.setTotalPage(totalPage);
        getUserBrandsResponse.setBrandList(list);
        getUserBrandsResponse.setEnded(isEnded);

        return getUserBrandsResponse;
    }

    @Transactional
    public void scheduleBrandFavorite() {
        Set<String> keys = cacheTemplate.getKeys("favorite");
        for (String key : keys) {
            int value = Integer.parseInt(cacheTemplate.getValue(key));
            Long brandId = Long.parseLong(cacheTemplate.parseCashNameKey(key).get("key"));
            brandDomainService.updateBrandFavoriteCount(brandId, value);
            cacheTemplate.deleteCache(key);
        }
    }

    public List<String> getBrandSearchKeyword() {
        List<String> list = new ArrayList<>();
        list.add("volvik");
        list.add("brnad1");
        list.add("brnad2");
        list.add("brnad3");
        list.add("brnad4");
        list.add("brnad5");
        list.add("brnad6");
        list.add("brnad7");
        list.add("brnad8");
        list.add("brnad9");

        return list;
    }


    public void getBrandTagList() {

    }

}
