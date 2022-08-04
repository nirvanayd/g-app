package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.MarketingTypeConverter;
import com.nelly.application.converter.StyleTypeConverter;
import com.nelly.application.enums.MarketingType;
import com.nelly.application.enums.StyleType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "user_marketing")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class UserMarketing extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Convert(converter = MarketingTypeConverter.class)
    @Column(name = "marketing_code", nullable = false, length = 50)
    private MarketingType marketingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;
}


