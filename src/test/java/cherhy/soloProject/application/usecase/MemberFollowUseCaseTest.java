package cherhy.soloProject.application.usecase;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberFollowUseCaseTest {

    @Autowired
    MemberBlockReadService memberBlockReadService;
    @Autowired
    MemberBlockWriteService memberBlockWriteService;
    @Autowired
    MemberReadService memberReadService;

    @Test
    public void test(){
        Member myMember = memberReadService.getMember(17L);
        Member followMember = memberReadService.getMember(18L);
        memberBlockReadService.getBlockMember(myMember,followMember)
                .ifPresent(memberBlock -> assertThat(memberBlock.getId()).isEqualTo(7L));
    }

}