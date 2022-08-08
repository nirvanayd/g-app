package com.nelly.application.service.brand;

import com.nelly.application.domain.*;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.dto.request.GetRankRequest;
import com.nelly.application.dto.request.GetUserBrandsRequest;
import com.nelly.application.dto.request.SaveUserBrandsRequest;
import com.nelly.application.dto.response.BrandRankResponse;
import com.nelly.application.dto.response.GetRankResponse;
import com.nelly.application.enums.YesOrNoType;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.BrandDomainService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.net.MalformedURLException;
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

    public Brands getBrand(Long brandId) {
        return brandDomainService.selectBrand(brandId);
    }

    public void addCurrentItem(AddCurrentItemRequest dto) {
        try {
            Users user = userService.getUser();
            UrlInfoDto urlInfoDto = UrlUtil.parseUrl(dto.getUrl());
            ScraperBrandDetails detail = scraperService.getScraperBrand(urlInfoDto);
            String moduleName = detail.getScraperBrand().getModuleName();
            ItemScrapDto itemScrapDto = scraperManager.addCurrentItem(dto.getUrl(), moduleName);

            // response check

            // create scrap log

            // image s3 upload


        } catch (MalformedURLException me) {

        }
    }

    public void downloadImage() {
        String url = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/40b32ba35f6b41dc801dadcb0119de47_9366/Black_GZ3901_01_standard.jpg";
    }

    public GetRankResponse getBrandRankList(GetRankRequest getRankRequest) {

        Page<BrandRank> brandsPage = brandDomainService.selectAppBrandRankList(getRankRequest.getPage(), getRankRequest.getSize());

        Optional<Users> user = userService.getAppUser();

        long totalCount = brandsPage.getTotalElements();
        long totalPage = brandsPage.getTotalPages();
        List<BrandRank> rankList = brandsPage.getContent();

        List<BrandRankResponse> list = rankList.stream().
                map(u -> modelMapper.map(u.getBrand(), BrandRankResponse.class)).collect(Collectors.toList());

        List<Long> brandIdList = list.stream().map(BrandRankResponse::getId).collect(Collectors.toList());

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

    public GetRankResponse getUserBrandList(Long userId, GetUserBrandsRequest getUserBrandsRequest) {

        Page<UserBrands> userBrandsPage = brandDomainService.selectUserBrandList(userId,
                getUserBrandsRequest.getPage(), getUserBrandsRequest.getSize());

        long totalCount = userBrandsPage.getTotalElements();
        long totalPage = userBrandsPage.getTotalPages();
        List<UserBrands> userBrandList = userBrandsPage.getContent();
        List<BrandRankResponse> list = userBrandList.stream().
                map(u -> modelMapper.map(u.getBrand(), BrandRankResponse.class)).collect(Collectors.toList());

        list.forEach(l -> l.setIsFavorite(true));

        GetRankResponse getRankResponse = new GetRankResponse();
        getRankResponse.setTotalCount(totalCount);
        getRankResponse.setTotalPage(totalPage);
        getRankResponse.setBrandList(list);

        return getRankResponse;
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
}
