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
@Table(name = "content_likes", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "content_id" }) })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ContentLikes extends BaseTime{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content_id")
    private Long contentId;
}

