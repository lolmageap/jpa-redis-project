package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.domain.memberBlock.repository.jpa.MemberBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberBlockWriteService {

    private final MemberBlockRepository memberBlockRepository;

    public void buildMemberBlock(Member member, Member blockMember) {
        MemberBlock buildMemberBlock = MemberBlock.builder().member(member).blockMember(blockMember).build();
        block(buildMemberBlock);
    }

    public void block(MemberBlock memberBlock) {
        memberBlockRepository.save(memberBlock);
    }

    public void unblock(MemberBlock memberBlock) {
        memberBlockRepository.delete(memberBlock);
    }

}
