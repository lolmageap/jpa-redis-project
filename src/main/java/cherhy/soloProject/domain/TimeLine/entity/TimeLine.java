package cherhy.soloProject.domain.TimeLine.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TIMELINE",
        indexes = {@Index(name = "timeline__index__member_id", columnList = "member_id")})
public class TimeLine extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeline_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    private Post post;
//    @OneToMany
//    private List<Post> posts = new ArrayList<>(); //List

    @Builder
    public TimeLine(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

}
