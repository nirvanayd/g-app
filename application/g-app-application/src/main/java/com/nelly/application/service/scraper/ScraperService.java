package com.nelly.application.service.scraper;

import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.ScraperLog;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.enums.ScraperLogResult;
import com.nelly.application.service.ScraperDomainService;
import com.nelly.application.util.ScraperManager;
import com.nelly.application.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
public class ScraperService {

    private final ScraperDomainService scraperDomainService;
    private final ScraperManager scraperManager;

    public ScraperBrandDetails getScraperBrand(UrlInfoDto urlInfoDto) throws MalformedURLException {
        List<ScraperBrandDetails> detailList = scraperDomainService.selectScraperBrandDetail(urlInfoDto.getHost());

        if (detailList.size() == 0) throw new RuntimeException("브랜드 정보를 조회할 수 없습니다.");

        return detailList.stream().filter(d -> urlInfoDto.getPath().contains(d.getItemPath())).findFirst()
                .orElseThrow(() -> new RuntimeException("제품 URL 경로가 일치하지 않습니다."));
    }

    public void saveScrapRequest(WebviewRequest dto, Users user) throws MalformedURLException {
        scraperDomainService.createScraperRequest(dto.getUrl(), user == null ? null : user.getId());
        UrlInfoDto urlInfoDto = UrlUtil.parseUrl(dto.getUrl());
    }

    public ScraperBrands searchScraperBrand(WebviewRequest dto) throws MalformedURLException {
        UrlInfoDto urlInfoDto = UrlUtil.parseUrl(dto.getUrl());
        List<ScraperBrands> list = scraperDomainService.selectScraperBrand(urlInfoDto.getHost());
        String[] pairs = urlInfoDto.getQuery() == null ? null : urlInfoDto.getQuery().split("&");

        Optional<ScraperBrands> a = list.stream().filter(l -> {
            try {
                UrlInfoDto storeInfo = UrlUtil.parseUrl(l.getStoreUrl());
                String query = storeInfo.getQuery();
                if (query == null || query.isEmpty()) return true;
                return Arrays.asList(pairs).contains(query);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst();

        if (a.isEmpty()) return null;
        return a.get();
    }

    public void saveHistory(Users user, ScraperBrands brand, String url) throws MalformedURLException {
        Long userId = (user == null ) ? null : user.getId();
        UrlInfoDto urlInfoDto = UrlUtil.parseUrl(url);
        // 상품 상세인 경우 히스토리에 저장함.
        List<ScraperBrandDetails> detailList = scraperDomainService.selectScraperBrandDetail(urlInfoDto.getHost());
        if (detailList.size() == 0) return;

        ScraperBrandDetails detail = detailList.stream().
                filter(d -> d.getItemPath() == null || urlInfoDto.getPath().contains(d.getItemPath())).findFirst()
                .orElse(null);
        if (detail == null) return;

        // log 초기화
        ScraperLog scraperLog = scrapLogInit(userId, brand.getId(), url);

        // scrap 요청
        String moduleName = brand.getModuleName();
        // 비동기 처리
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ItemScrapDto itemScrapDto = scraperManager.addCurrentItem(url, moduleName);
                scraperLogUpdate(scraperLog.getId(), itemScrapDto.getImageList(), itemScrapDto.getName(), itemScrapDto.getPrice(), ScraperLogResult.SUCCESS.getCode());
                Long existScrapItemId = scraperDomainService.selectScrapItem(url);
                if (existScrapItemId == null) scraperDomainService.createScrapItems(url, itemScrapDto.getName(),
                        0, "kr");
            }
        });
        thread.start();
    }

    private ScraperLog scrapLogInit(Long userId, Long scrapBrandId, String targetUrl) {
        return scraperDomainService.createScrapLog(scrapBrandId, targetUrl, userId);
    }

    private void scraperLogUpdate(Long scraperLogId, List<String> imageList, String name, String price, String resultCode) {
        scraperDomainService.updateScrapLog(scraperLogId, imageList, name, price, resultCode);
    }
}
