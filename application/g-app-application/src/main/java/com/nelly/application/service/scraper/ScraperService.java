package com.nelly.application.service.scraper;

import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.service.ScraperDomainService;
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

    public ScraperBrandDetails getScraperBrand(UrlInfoDto urlInfoDto) throws MalformedURLException {
        List<ScraperBrandDetails> detailList = scraperDomainService.selectScraperBrandDetail(urlInfoDto.getHost());

        if (detailList.size() == 0) throw new RuntimeException("브랜드 정보를 조회할 수 없습니다.");

        return detailList.stream().filter(d -> urlInfoDto.getPath().contains(d.getItemPath())).findFirst()
                .orElseThrow(() -> new RuntimeException("제품 URL 경로가 일치하지 않습니다."));
    }

    public void saveScrapRequest(WebviewRequest dto, Users user) {
        scraperDomainService.createScraperRequest(dto.getUrl(), user == null ? null : user.getId());
    }

    public Long searchScraperBrand(WebviewRequest dto) throws MalformedURLException {
        UrlInfoDto urlInfoDto = UrlUtil.parseUrl(dto.getUrl());

        List<ScraperBrands> list = scraperDomainService.selectScraperBrand(urlInfoDto.getHost());
        String[] pairs = urlInfoDto.getQuery().split("&");

        Optional<ScraperBrands> a = list.stream().filter(l -> {
            try {
                UrlInfoDto storeInfo = UrlUtil.parseUrl(l.getStoreUrl());
                String query = storeInfo.getQuery();
                return Arrays.asList(pairs).contains(query);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst();

        if (a.isEmpty()) return null;
        return a.get().getId();
    }
}
