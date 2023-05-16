package cherhy.soloProject.domain.follow.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FOLLOW",
        indexes = {@Index(name = "follower__index__member_id", columnList = "follower_member_id")
                , @Index(name = "following__index__post_id", columnList = "following_member_id")})
public class Follow extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member following;

    public static Follow of(Member follower, Member following){
       return Follow.builder()
                .follower(follower)
                .following(following)
                .build();
    }

    @Builder
    private Follow(Member follower, Member following) {
        this.follower = follower;
        this.following = following;
    }

}
