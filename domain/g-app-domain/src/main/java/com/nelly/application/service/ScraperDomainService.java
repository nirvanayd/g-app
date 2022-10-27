package com.nelly.application.service;

import com.google.gson.Gson;
import com.nelly.application.domain.*;
import com.nelly.application.enums.ScraperLogResult;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScraperDomainService {

    private final ScraperBrandDetailsRepository scraperBrandDetailsRepository;
    private final ScraperBrandsRepository scraperBrandsRepository;
    private final ScraperLogRepository scraperSimulationLogRepository;
    private final ScrapItemsRepository scrapItemsRepository;
    private final ScrapItemImagesRepository scrapItemImagesRepository;
    private final ScraperLogRepository scraperLogRepository;
    private final ScraperRequestRepository scraperRequestRepository;


    public List<ScraperBrandDetails> selectScraperBrandDetail(String host) {
        return scraperBrandDetailsRepository.findAllByHost(host);
    }

    public List<ScraperBrands> selectAllScraperBrands() {
        return scraperBrandsRepository.findAll();
    }

    public void createScraperSimulationLog(String name, String price, String imageList, Long scraperBrandId,
                                           String targetUrl, String resultCode) {

        ScraperLog log = ScraperLog.builder()
                .name(name)
                .price(price)
                .imageList(imageList)
                .scraperBrandId(scraperBrandId)
                .targetUrl(targetUrl)
                .resultCode(resultCode)
                .build();

        scraperSimulationLogRepository.save(log);
    }

    public ScrapItems createScrapItems(String url, String name, Integer price, String currency) {
        ScrapItems scrapItem = ScrapItems.builder()
                .url(url)
                .name(name)
                .price(price)
                .currency(currency)
                .build();

        return scrapItemsRepository.save(scrapItem);
    }

    public void createScrapItemImages(ScrapItems ScrapItem, String imageUrl) {
        ScrapItemImages scrapItemImage = ScrapItemImages.builder()
                .scrapItem(ScrapItem)
                .imageUrl(imageUrl)
                .build();

        scrapItemImagesRepository.save(scrapItemImage);
    }

    public ScraperLog createScrapLog(Long brandId, String targetUrl, Long userId) {
        ScraperLog log = ScraperLog.builder()
                .scraperBrandId(brandId)
                .targetUrl(targetUrl)
                .userId(userId)
                .resultCode(ScraperLogResult.INIT.getCode())
                .build();
        return scraperLogRepository.save(log);
    }

    public void updateScrapLog(Long scraperLogId, List<String> imageList, String name, String price, String resultCode) {
        ScraperLog log = scraperLogRepository.findById(scraperLogId).orElseThrow(() -> new RuntimeException("로그를 조회할 수 없습니다."));
        Gson gson = new Gson();
        log.setName(name);
        log.setImageList(gson.toJson(imageList));
        log.setPrice(price);
        log.setResultCode(resultCode);
        scraperLogRepository.save(log);
    }

    public void createScraperRequest(String url, Long userId) {
        ScraperRequest scraperRequest = ScraperRequest.builder()
                .targetUrl(url)
                .userId(userId)
                .build();
        scraperRequestRepository.save(scraperRequest);
    }

    public List<ScraperBrands> selectScraperBrand(String host) {
        return scraperBrandsRepository.findByStoreUrlContains(host);
    }

    public Long selectScrapItem(String url) {
        Optional<ScrapItems> scrapItem = scrapItemsRepository.findByUrl(url);
        return scrapItem.map(ScrapItems::getId).orElse(null);
    }
}
