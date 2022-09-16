package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.AgeTypeConverter;
import com.nelly.application.converter.DeleteStatusConverter;
import com.nelly.application.converter.YesOrNoTypeConverter;
import com.nelly.application.enums.DeleteStatus;
import com.nelly.application.enums.YesOrNoType;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET deleted_date = NOW() WHERE id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Comments extends BaseTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Contents content;

    @OneToMany(mappedBy = "parent")
    private List<Comments> comments = new ArrayList<>();

    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    @Convert(converter = DeleteStatusConverter.class)
    @Column(name = "status")
    private DeleteStatus status;
}
