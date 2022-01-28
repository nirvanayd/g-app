package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.StyleTypeConverter;
import com.nelly.application.enums.StyleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "brand_styles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class BrandStyles extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @Convert(converter = StyleTypeConverter.class)
    @Column(name = "style_code", nullable = false, length = 50)
    private StyleType brandStyle;
}
