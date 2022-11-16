package com.nelly.application.service;

import com.nelly.application.domain.Agreements;
import com.nelly.application.domain.ReportItems;
import com.nelly.application.repository.AgreementsRepository;
import com.nelly.application.repository.ReportItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppDomainService {

    private final AgreementsRepository agreementsRepository;
    private final ReportItemsRepository reportItemsRepository;

    public List<Agreements> selectAgreements(String version) {
        return agreementsRepository.findAllByVersionOrderBySeqAsc(version);
    }

    public List<ReportItems> selectReportItemList() {
        return reportItemsRepository.findAllByTypeOrderBySeqAsc("content");
    }
}
