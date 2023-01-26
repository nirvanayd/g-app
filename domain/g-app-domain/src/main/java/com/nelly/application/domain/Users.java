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
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Users extends BaseTime {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "auth_id", nullable = false)
    private Long authId;
    @Column(name = "login_id", nullable = false, length = 100)
    private String loginId;
    @Column(name = "role", nullable = false, length = 10)
    private String role;
    @Column(name = "email", nullable = false, length = 200)
    private String email;
    @Column(name = "birth", nullable = false, length = 10)
    private String birth;
    @Column(name = "profile_image_url", nullable = true, length = 500)
    private String profileImageUrl;

    @Column(name = "background_image_url", nullable = true, length = 500)
    private String backgroundImageUrl;

    @Column(name = "profile_title", nullable = true, length = 500)
    private String profileTitle;
    @Column(name = "profile_text", nullable = true, columnDefinition = "TEXT")
    private String profileText;
    @Convert(converter = UserStatusConverter.class)
    private UserStatus status;
    @Column(name = "following_count", nullable = true)
    private Integer followingCount;
    @Column(name = "follower_count", nullable = true)
    private Integer followerCount;
    @Column(name="joined_date", nullable = false)
    private LocalDateTime joinedDate;
    @Column(name="leave_date", nullable = true)
    private LocalDateTime leaveDate;

    @OneToMany(mappedBy = "user")
    private List<UserStyles> userStyles;

    @Formula("(select count(1) from contents c where c.user_id = id)")
    private Integer contentCount;

    @PrePersist
    public void prePersist() {
        this.followingCount = this.followingCount == null ? 0 : this.followingCount;
        this.followerCount = this.followerCount == null ? 0 : this.followerCount;
    }
}
