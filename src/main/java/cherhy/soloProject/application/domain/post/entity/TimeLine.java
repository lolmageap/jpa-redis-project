package cherhy.soloProject.application.domain.post.entity;

import cherhy.soloProject.application.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeLine {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeline_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public TimeLine(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

}
