package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "scraper_simulation_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ScraperSimulationLog extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="scraper_brand_id", nullable = false)
    private Long scraperBrandId;

    @Column(name="target_url", nullable = false)
    private String targetUrl;

    @Column(name="result_code", nullable = false)
    private String resultCode;

    @Column(name="price", nullable = false)
    private String price;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="imageList", nullable = false)
    private String imageList;
}
