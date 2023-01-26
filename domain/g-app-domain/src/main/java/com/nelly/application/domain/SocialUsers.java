package com.nelly.application.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.nelly.application.converter.UserStatusConverter;
import com.nelly.application.enums.UserStatus;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "social_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class SocialUsers extends BaseTime {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "auth_id", nullable = false)
    private Long authId;
    @Column(name = "uid", nullable = false, length = 100)
    private String uid;
    @Column(name="type", nullable = false, length = 2)
    private String type;
}
