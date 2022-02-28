package com.nelly.application.service.brand;

import com.nelly.application.domain.Brands;
import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.service.scraper.ScraperService;
import com.nelly.application.service.user.UserService;
import com.nelly.application.util.ScraperManager;
import com.nelly.application.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@Service
@Slf4j
public class BrandService {

    private final BrandDomainService brandDomainService;
    private final UserService userService;
    private final ScraperManager scraperManager;
    private final ScraperService scraperService;
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
            log.info("name : " + itemScrapDto.getName());
            log.info("price : " + itemScrapDto.getPrice());
            log.info("imageList : " + itemScrapDto.getImageList().size());
        } catch (MalformedURLException me) {

        }
    }
}
