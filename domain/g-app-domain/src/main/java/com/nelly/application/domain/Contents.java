package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.YesOrNoTypeConverter;
import com.nelly.application.enums.YesOrNoType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Table(name = "contents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Contents extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content_text", nullable = true, columnDefinition = "TEXT")
    private String contentText;

    @Column(name = "view_count", nullable = false, columnDefinition = "integer default 0")
    private Integer viewCount;

    @Column(name = "like_count", nullable = false, columnDefinition = "integer default 0")
    private Integer likeCount;

    @Column(name = "mark_count", nullable = false, columnDefinition = "integer default 0")
    private Integer markCount;

    @Column(name = "reply_count", nullable = false, columnDefinition = "integer default 0")
    private Integer replyCount;

    @Convert(converter = YesOrNoTypeConverter.class)
    @Column(name = "deleted_yn", nullable = false, length = 2)
    private YesOrNoType deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "content")
    private List<ContentImages> contentImages;

    @OneToMany(mappedBy = "content")
    private List<BrandHashTags> brandHashTags;

    @OneToMany(mappedBy = "content")
    private List<UserHashTags> userHashTags;

    @OneToMany(mappedBy = "content")
    private List<ContentHashTags> itemHashTags;
}
