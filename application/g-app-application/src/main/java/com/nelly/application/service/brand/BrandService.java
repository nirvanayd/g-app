package com.nelly.application.service.brand;

import com.nelly.application.domain.Brands;
import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.AddCurrentItemRequest;
import com.nelly.application.service.BrandDomainService;
import com.nelly.application.service.ScraperDomainService;
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
    private final ScraperDomainService scraperDomainService;
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
}
