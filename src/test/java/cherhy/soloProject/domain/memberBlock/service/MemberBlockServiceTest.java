package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponseDto;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.exception.MemberBlockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    MemberWriteService memberWriteService;
    @Autowired
    MemberBlockReadService memberBlockReadService;
    @Autowired
    MemberBlockWriteService memberBlockWriteService;

    @BeforeEach
    @Order(1)
    @DisplayName("회원 추가")
    void addMember(){
        // given
        MemberRequest memberRequest = new MemberRequest("abcdef", "정철희", "ekxk1234@gmail.com", "1234");
        memberWriteService.signUp(memberRequest);

        MemberRequest memberRequest2 = new MemberRequest("qwerty", "홍길동", "abcd234@naver.com", "12345");
        memberWriteService.signUp(memberRequest2);

        MemberRequest memberRequest3 = new MemberRequest("zxcvbn", "유재석", "zxcvbn@gmail.com", "1111");
        memberWriteService.signUp(memberRequest3);

        MemberRequest memberRequest4 = new MemberRequest("hihihi", "고양이", "noise@naver.com", "111111111");
        memberWriteService.signUp(memberRequest4);

        MemberRequest memberRequest5 = new MemberRequest("testtest", "유재석", "jzcxhvljk@gmail.com", "4444sdfwe");
        memberWriteService.signUp(memberRequest5);
    }

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

    @Test
    @Order(4)
    @DisplayName("차단한 사람들 조회")
    void testGetMemberBlocks(){
        // given
        Member me = memberReadService.getMember(3L);
        Member you = memberReadService.getMember(1L);
        Member you2 = memberReadService.getMember(2L);
        Member you3 = memberReadService.getMember(4L);
        Member you4 = memberReadService.getMember(5L);

        ScrollRequest scrollRequest = new ScrollRequest(null);

        // when
        MemberBlock memberToBlock1 = MemberBlock.of(me, you);
        memberBlockWriteService.block(memberToBlock1);

        MemberBlock memberToBlock2 = MemberBlock.of(me, you2);
        memberBlockWriteService.block(memberToBlock2);

        MemberBlock memberToBlock3 = MemberBlock.of(me, you3);
        memberBlockWriteService.block(memberToBlock3);

        MemberBlock memberToBlock4 = MemberBlock.of(me, you4);
        memberBlockWriteService.block(memberToBlock4);

        List<MemberBlockResponseDto> memberBlocks = memberBlockReadService.getMemberBlocks(me, scrollRequest);
        long nextKey = memberBlockReadService.getNextKey(memberBlocks);

        //then
        Assertions.assertThat(memberBlocks.size()).isEqualTo(3);
        Assertions.assertThat(nextKey).isEqualTo(2); // Id desc
    }

}