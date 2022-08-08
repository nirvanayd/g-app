package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.StyleTypeConverter;
import com.nelly.application.enums.StyleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "user_brands",
uniqueConstraints = { //other constraints
        @UniqueConstraint(name = "UniqueUserBrands", columnNames = { "brand_id", "user_id" })})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserBrands extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "user_id")
    private Long userId;
}


