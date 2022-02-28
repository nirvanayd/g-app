package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "scraper_brands")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ScraperBrands extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="name", nullable = true)
    private String name;

    @Column(name="module_name", nullable = true)
    private String moduleName;

    @Column(name="is_used", nullable = true)
    private boolean isUsed;

    @Column(name="sample_url", nullable = true)
    private String sampleUrl;

    @Column(name="last_simulation_datetime", nullable = true)
    private LocalDateTime lastSimulationDatetime;

    @OneToMany(mappedBy = "scraperBrand")
    private List<ScraperBrandDetails> scraperBrandDetails;
}
