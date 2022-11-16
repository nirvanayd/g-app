package com.nelly.application.repository;

import com.nelly.application.domain.Contents;
import com.nelly.application.domain.ReportItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportItemsRepository extends JpaRepository<ReportItems, Long> {
    List<ReportItems> findAllByTypeOrderBySeqAsc(String type);
}
