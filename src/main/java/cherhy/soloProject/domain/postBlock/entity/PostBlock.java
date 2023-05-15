package cherhy.soloProject.domain.postBlock.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;

@Entity @Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostBlock extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static PostBlock of(Member member, Post post){
        return PostBlock.builder()
                .member(member)
                .post(post)
                .build();
    }

    @Builder
    private PostBlock(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
