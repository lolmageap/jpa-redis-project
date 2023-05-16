package cherhy.soloProject.domain.memberBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.memberBlock.dto.response.MemberBlockResponse;
import cherhy.soloProject.domain.memberBlock.entity.MemberBlock;
import cherhy.soloProject.exception.MemberBlockException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Rollback
@Transactional
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberBlockServiceTest {
    @Autowired
    MemberBlockReadService memberBlockReadService;
    @Autowired
    MemberBlockWriteService memberBlockWriteService;

    EntityManager em;


    @Test
    @DisplayName("차단")
    void testBlock(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        // when
        MemberBlock memberToBlock = MemberBlock.of(me, you);
        memberBlockWriteService.block(memberToBlock);

        em.flush();
        em.clear();

        // then
        Optional<MemberBlock> result = memberBlockReadService.getBlockMember(me, you);
        Assertions.assertThat(result.get().getMember().getName()).isEqualTo("정철희");
    }

    @Test
    @DisplayName("차단 조회 Error")
    void testGetBlockError(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        // then
        assertThatThrownBy(() -> memberBlockReadService.getBlockMember(me, you))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    @DisplayName("내가 차단 당했는지 확인")
    void testIfBlocked(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        // when
        MemberBlock memberToBlock = MemberBlock.of(me, you);
        memberBlockWriteService.block(memberToBlock);

        em.flush();
        em.clear();

        // then
        memberBlockReadService.ifIBlock(me, you);
    }

    @Test
    @DisplayName("내가 차단 당했는지 확인 Error")
    void testIfBlockedError(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        // then
        assertThatThrownBy(() -> memberBlockReadService.ifIBlock(me, you))
                .isInstanceOf(MemberBlockException.class);
    }

    @Test
    @DisplayName("차단풀기")
    void testUnblock(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        // when
        MemberBlock blockMember = MemberBlock.of(me, you);
        memberBlockWriteService.block(blockMember);
        memberBlockWriteService.unblock(blockMember);

        em.flush();
        em.clear();

        assertThatThrownBy(() -> memberBlockReadService.getBlockMember(me, you)
                                .orElseThrow(NullPointerException::new))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("차단한 사람들 조회")
    void testGetMemberBlocks(){
        // given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        Member you2 = Member.builder()
                .userId("qwerty")
                .name("홍길동")
                .email("abcd234@naver.com")
                .password("12345")
                .build();

        Member you3 = Member.builder()
                .userId("hihihi")
                .name("고양이")
                .email("noise@naver.com")
                .password("111111111")
                .build();

        Member you4 = Member.builder()
                .userId("testtest")
                .name("유재석")
                .email("jzcxhvljk@naver.com")
                .password("4444sdfwe")
                .build();

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

        // Order By Id Desc
        List<MemberBlockResponse> memberBlocks = memberBlockReadService.getMemberBlocks(me, scrollRequest);
        long nextKey = memberBlockReadService.getNextKey(memberBlocks);

        //then
        Assertions.assertThat(memberBlocks.size()).isEqualTo(3);
        Assertions.assertThat(nextKey).isEqualTo(2);
    }

}