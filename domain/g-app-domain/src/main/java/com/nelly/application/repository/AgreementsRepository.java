package com.nelly.application.repository;

import com.nelly.application.domain.Agreements;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementsRepository extends JpaRepository<Agreements, Long> {
    List<Agreements> findAllByVersionOrderBySeqAsc(String version);
}
