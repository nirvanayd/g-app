package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.AgreementTypeConverter;
import com.nelly.application.converter.YesOrNoTypeConverter;
import com.nelly.application.enums.YesOrNoType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "agreements")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Agreements extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_type_code", nullable = false, length = 10)
    private String agreementTypeCode;

    @Column(name = "version", nullable = false, length = 10)
    private String version;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "is_required", nullable = false, length = 255)
    private String isRequired;

    @Column(name = "content", nullable = true, columnDefinition = "TEXT")
    private String content;
}
