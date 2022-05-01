package com.nelly.application.service;

import com.nelly.application.domain.*;
import com.nelly.application.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScraperDomainService {

    private final ScraperBrandDetailsRepository scraperBrandDetailsRepository;
    private final ScraperBrandsRepository scraperBrandsRepository;
    private final ScraperLogRepository scraperSimulationLogRepository;
    private final ScrapItemsRepository scrapItemsRepository;
    private final ScrapItemImagesRepository scrapItemImagesRepository;


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

}
