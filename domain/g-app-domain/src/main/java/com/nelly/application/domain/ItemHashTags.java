package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.HashTagTypeConverter;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Table(name = "item_hash_tags")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ItemHashTags extends BaseTime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tag", nullable = false, length = 100)
    private String name;

//    @Column(name = "item_id", nullable = true)
//    private long keyId;
}
