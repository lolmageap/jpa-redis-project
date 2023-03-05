package cherhy.soloProject.domain.memberBlock.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBlock extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member blockMember;

    @Builder
    public MemberBlock(Member member, Member blockMember) {
        this.member = member;
        this.blockMember = blockMember;
    }
}
