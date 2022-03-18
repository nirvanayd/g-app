package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.BrandStatusConverter;
import com.nelly.application.converter.DisplayTypeConverter;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.DisplayType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Table(name = "scrap_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ScrapItems extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "currency", nullable = false)
    private String currency;

    @OneToMany(mappedBy = "scrapItem")
    private List<ScrapItemImages> scrapItemImages;
}
