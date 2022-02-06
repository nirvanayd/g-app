package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.PlaceTypeConverter;
import com.nelly.application.enums.PlaceType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "brand_places")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class BrandPlaces extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @Convert(converter = PlaceTypeConverter.class)
    @Column(name = "place_code", nullable = false, length = 50)
    private PlaceType placeType;
}
