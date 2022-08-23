package com.nelly.application.service.scraper;

import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.ScraperRequest;
import com.nelly.application.domain.Users;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.service.ScraperDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.List;

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
}
