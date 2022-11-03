package com.nelly.application.service.scraper;

import com.nelly.application.domain.*;
import com.nelly.application.dto.ItemScrapDto;
import com.nelly.application.dto.UrlInfoDto;
import com.nelly.application.dto.request.GetUserCartRequest;
import com.nelly.application.dto.request.GetUserCurrentScrapItemRequest;
import com.nelly.application.dto.request.PageRequest;
import com.nelly.application.dto.request.WebviewRequest;
import com.nelly.application.dto.response.CommentResponse;
import com.nelly.application.dto.response.GetUserCartResponse;
import com.nelly.application.dto.response.GetUserCurrentScrapItemResponse;
import com.nelly.application.dto.response.ScrapItemResponse;
import com.nelly.application.enums.ScraperLogResult;
import com.nelly.application.exception.NoContentException;
import com.nelly.application.exception.SystemException;
import com.nelly.application.service.ScraperDomainService;
import com.nelly.application.util.ScraperManager;
import com.nelly.application.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
@Slf4j
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

        if (a.isEmpty() && dto.getId() != null) {
            Optional<ScraperBrands> b = list.stream().filter(l -> l.getId().equals(dto.getId())).findFirst();
            if (b.isPresent()) return b.get();
        }
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
                ScrapItems existScrapItem = scraperDomainService.selectScrapItem(url).orElse(null);
                if (existScrapItem == null) {
                    ScrapItems scrapItem = scraperDomainService.createScrapItems(url, itemScrapDto.getName(),
                            Integer.parseInt(itemScrapDto.getPrice().replaceAll("[^0-9]", "")), "kr",
                            brand.getId(), brand.getName());
                    scraperDomainService.createScrapItemImages(scrapItem, itemScrapDto.getImageList());
                    if (userId != null) {
                        scraperDomainService.updateUserScrapHistory(scrapItem, userId);
                    }
                } else {
                    if (userId != null) {
                        scraperDomainService.updateUserScrapHistory(existScrapItem, userId);
                    }
                }
            }
        });
        thread.start();
    }

    public void addCart(WebviewRequest dto, Users user) {
        Optional<ScrapItems> existScrapItem = scraperDomainService.selectScrapItem(dto.getUrl());
        if (existScrapItem.isEmpty()) return;
        // 아이템 없는 경우 재저장 고려
        if (scraperDomainService.selectUserScrapCart(existScrapItem.get(), user.getId()).isPresent()) {
            throw new SystemException("이미 장바구니에 추가된 제품입니다.");
        }
        scraperDomainService.saveUserScrapCart(existScrapItem.get(), user.getId());
    }

    public GetUserCurrentScrapItemResponse getCurrentScrapItemList(Users user, GetUserCurrentScrapItemRequest dto) {
        Page<UserScrapHistory> existScrapHistory = scraperDomainService.selectUserScrapItemList(user, dto.getPage(), dto.getSize());
        if (existScrapHistory.isEmpty()) {
            throw new NoContentException();
        }
        List<UserScrapHistory> userScrapHistoryList = existScrapHistory.getContent();
        ScrapItemResponse scrapItemResponse = new ScrapItemResponse();

        GetUserCurrentScrapItemResponse response = new GetUserCurrentScrapItemResponse();
        response.setTotalCount(existScrapHistory.getTotalElements());
        response.setList(scrapItemResponse.toDtoList(userScrapHistoryList));
        return response;
    }

    public GetUserCartResponse getScrapCartList(Users user, GetUserCartRequest dto) {
        Page<UserScrapCart> existUserCart = scraperDomainService.selectUserScrapCartList(user, dto.getPage(), dto.getSize());
        if (existUserCart.isEmpty()) {
            throw new NoContentException();
        }

        List<UserScrapCart> userScrapHistoryList = existUserCart.getContent();
        ScrapItemResponse scrapItemResponse = new ScrapItemResponse();

        GetUserCartResponse response = new GetUserCartResponse();
        response.setTotalCount(existUserCart.getTotalElements());
        response.setList(scrapItemResponse.cartToDtoList(userScrapHistoryList));
        return response;
    }

    private ScraperLog scrapLogInit(Long userId, Long scrapBrandId, String targetUrl) {
        return scraperDomainService.createScrapLog(scrapBrandId, targetUrl, userId);
    }

    private void scraperLogUpdate(Long scraperLogId, List<String> imageList, String name, String price, String resultCode) {
        scraperDomainService.updateScrapLog(scraperLogId, imageList, name, price, resultCode);
    }
}
