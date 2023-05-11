package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.exception.MemberBlockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Rollback
@Transactional
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberBlockServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberBlockReadService memberBlockReadService;
    @Autowired
    MemberBlockWriteService memberBlockWriteService;

    @Test
    @Order(1)
    @DisplayName("차단하기")
    void testBlock(){
        // given
        Member member = memberReadService.getMember(1L);
        Member blockMember = memberReadService.getMember(2L);

        // when
        MemberBlock memberToBlock = MemberBlock.of(member, blockMember);
        memberBlockWriteService.block(memberToBlock);

        // then
        Optional<MemberBlock> result = memberBlockReadService.getBlockMember(member, blockMember);
        Assertions.assertThat(result.get().getBlockMember().getId()).isEqualTo(2L);
    }


    @Test
    @Order(2)
    @DisplayName("내가 차단 당했는지 확인")
    void testGetBlock(){
        // given
        Member member = memberReadService.getMember(1L);
        Member blockMember = memberReadService.getMember(2L);

        // when
        MemberBlock memberToBlock = MemberBlock.of(member, blockMember);
        memberBlockWriteService.block(memberToBlock);

        // then
        assertThrows(MemberBlockException.class, () -> memberBlockReadService.ifIBlock(blockMember, member));
        memberBlockReadService.ifIBlock(member, blockMember);
    }

    @Test
    @Order(3)
    @DisplayName("차단풀기")
    void testUnblock(){
        // given
        Member member = memberReadService.getMember(1L);
        Member blockMember = memberReadService.getMember(2L);
        Optional<MemberBlock> unBlockMember = memberBlockReadService.getBlockMember(member, blockMember);

        // when
        unBlockMember.ifPresent(m -> memberBlockWriteService.unblock(m));

        // then
        assertThrows(NullPointerException.class, () ->
            memberBlockReadService.getBlockMember(member, blockMember)
                    .orElseThrow(NullPointerException::new)
        );
    }

}