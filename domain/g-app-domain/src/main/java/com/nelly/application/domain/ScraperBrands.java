package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.DisplayTypeConverter;
import com.nelly.application.converter.IsUsedTypeConverter;
import com.nelly.application.converter.ScraperStatusConverter;
import com.nelly.application.enums.IsUsedType;
import com.nelly.application.enums.ScraperStatus;
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

    @Convert(converter = IsUsedTypeConverter.class)
    private IsUsedType isUsed;

    @Column(name="sample_url", nullable = true)
    private String sampleUrl;

    @Convert(converter = ScraperStatusConverter.class)
    private ScraperStatus status;

    @Column(name="last_simulation_datetime", nullable = true)
    private LocalDateTime lastSimulationDatetime;

    @OneToMany(mappedBy = "scraperBrand")
    private List<ScraperBrandDetails> scraperBrandDetails;

    @Column(name="brand_url", nullable = true)
    private String brand_url;

    @Column(name="store_url", nullable = true)
    private String storeUrl;

    @Column(name="brand_id", nullable = true)
    private Long brandId;
}
