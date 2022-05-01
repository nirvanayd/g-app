package com.nelly.application.service.scraper;

import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.service.ScraperDomainService;
import com.nelly.application.util.ScraperManager;
import com.nelly.application.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScraperService {

    private final ScraperDomainService scraperDomainService;
    private final ScraperManager scraperManager;

    public ScraperBrandDetails getScraperBrand(UrlInfoDto urlInfoDto) throws MalformedURLException {
        List<ScraperBrandDetails> detailList = scraperDomainService.selectScraperBrandDetail(urlInfoDto.getHost());

        if (detailList.size() == 0) throw new RuntimeException("브랜드 정보를 조회할 수 없습니다.");

        return detailList.stream().filter(d -> d.getItemPath() == null || urlInfoDto.getPath().contains(d.getItemPath())).findFirst()
                .orElse(null);
    }

    public void scrapAll()  {
        List<ScraperBrands> scraperBrandList = scraperDomainService.selectAllScraperBrands();

        for(ScraperBrands brand : scraperBrandList) {
            if (!brand.getIsUsed().getCode()) continue;
            try {
                UrlInfoDto urlInfoDto = UrlUtil.parseUrl(brand.getSampleUrl());
                ScraperBrandDetails detail = getScraperBrand(urlInfoDto);
                String moduleName = brand.getModuleName();
                ItemScrapDto itemScrapDto = scraperManager.addCurrentItem(brand.getSampleUrl(), moduleName);
                if (itemScrapDto.getName() == null || itemScrapDto.getPrice() == null || itemScrapDto.getImageList().size() == 0) {
                    // 크롤러 오류
                }
            } catch (MalformedURLException me) {

            }
        }
    }

    public List<ScraperBrands> getScraperBrands() {
        return scraperDomainService.selectAllScraperBrands();
    }
}