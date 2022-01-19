package com.nelly.application.domain;

import com.nelly.application.converter.AgreementTypeConverter;
import com.nelly.application.converter.YesOrNoTypeConverter;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Agreements extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Convert(converter = AgreementTypeConverter.class)
    private String agreementTypeCode;
    @Column(name = "version", nullable = false, length = 10)
    private String version;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Convert(converter = YesOrNoTypeConverter.class)
    private String isRequired;
    @Column(name = "content", nullable = true, columnDefinition = "TEXT")
    private String content;
}
