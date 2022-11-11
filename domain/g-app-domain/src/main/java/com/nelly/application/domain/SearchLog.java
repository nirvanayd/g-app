package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.BrandStatusConverter;
import com.nelly.application.converter.SearchLogTypeConverter;
import com.nelly.application.enums.SearchLogType;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "search_log")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class SearchLog extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="user_id", nullable = true)
    private Long userId;

    @Column(name="keyword", nullable = false)
    private String keyword;

    @Convert(converter = SearchLogTypeConverter.class)
    @Column(name="type", nullable = false)
    private SearchLogType searchLogType;
}
