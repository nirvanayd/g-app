package com.nelly.application.service;

import com.nelly.application.domain.ScraperBrandDetails;
import com.nelly.application.domain.ScraperBrands;
import com.nelly.application.domain.ScraperSimulationLog;
import com.nelly.application.repository.ScraperBrandDetailsRepository;
import com.nelly.application.repository.ScraperBrandsRepository;
import com.nelly.application.repository.ScraperSimulationLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ScraperDomainService {

    private final ScraperBrandDetailsRepository scraperBrandDetailsRepository;
    private final ScraperBrandsRepository scraperBrandsRepository;
    private final ScraperSimulationLogRepository scraperSimulationLogRepository;


    public List<ScraperBrandDetails> selectScraperBrand(String host) {
        return scraperBrandDetailsRepository.findAllByHost(host);
    }

    public List<ScraperBrands> selectAllScraperBrands() {
        return scraperBrandsRepository.findAll();
    }

    public void createScraperSimulationLog(String name, String price, String imageList, Long scraperBrandId,
                                           String targetUrl, String resultCode) {

        ScraperSimulationLog log = ScraperSimulationLog.builder()
                .name(name)
                .price(price)
                .imageList(imageList)
                .scraperBrandId(scraperBrandId)
                .targetUrl(targetUrl)
                .resultCode(resultCode)
                .build();

        scraperSimulationLogRepository.save(log);
    }
}
