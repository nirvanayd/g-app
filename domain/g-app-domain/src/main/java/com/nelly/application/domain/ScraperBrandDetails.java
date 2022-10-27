package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "scraper_brand_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ScraperBrandDetails extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @Column(name="is_https", nullable = true)
//    private boolean isHttps;

    @Column(name="host", nullable = true)
    private String host;

    @Column(name="port", nullable = true)
    private Integer port;

    @Column(name="item_path", nullable = true)
    private String itemPath;

    @Column(name="query", nullable = true)
    private String query;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scraper_brand_id")
    private ScraperBrands scraperBrand;
}
