package cherhy.soloProject.application.domain.postBlock.entity;

import cherhy.soloProject.Util.BaseEntity;
import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostBlock extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public PostBlock(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
