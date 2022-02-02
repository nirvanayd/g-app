package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.BrandStatusConverter;
import com.nelly.application.converter.UserStatusConverter;
import com.nelly.application.enums.BrandStatus;
import com.nelly.application.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Table(name = "brands")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Brands extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "logo_image_url", nullable = true, length = 500)
    private String logoImageUrl;
    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;
    @Convert(converter = BrandStatusConverter.class)
    private BrandStatus status;
    @Column(name="is_display", columnDefinition = "TINYINT", length=1)
    private Integer isDisplay;
    @Column(name = "homepage", nullable = false, length = 500)
    private String homepage;
    @Column(name = "introduce_image_url", nullable = true, length = 500)
    private String introduceImageUrl;

    @OneToMany(mappedBy = "brand")
    private List<BrandStyles> brandStyles;

    @OneToMany(mappedBy = "brand")
    private List<BrandPlaces> brandPlaces;

    @OneToMany(mappedBy = "brand")
    private List<BrandAges> brandAges;

    @PrePersist
    public void prePersist() {
        this.isDisplay = this.isDisplay == null ? 0 : this.isDisplay;
    }
}