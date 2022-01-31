package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.AgeTypeConverter;
import com.nelly.application.converter.PlaceTypeConverter;
import com.nelly.application.enums.AgeType;
import com.nelly.application.enums.PlaceType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "brand_ages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class BrandAges extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @Convert(converter = AgeTypeConverter.class)
    @Column(name = "age_code", nullable = false, length = 50)
    private AgeType ageType;
}
