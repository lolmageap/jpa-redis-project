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
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

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
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

        // then
        assertThatThrownBy(() -> memberBlockReadService.getBlockMember(me, you))
                .isInstanceOf(NullPointerException.class);

    }

    @Test
    @DisplayName("내가 차단 당했는지 확인")
    void testIfBlocked(){
        // given
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

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
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

        // then
        assertThatThrownBy(() -> memberBlockReadService.ifIBlock(me, you))
                .isInstanceOf(MemberBlockException.class);
    }

    @Test
    @DisplayName("차단풀기")
    void testUnblock(){
        // given
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

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
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");
        Member you2 = getMember("qwerty","홍길동","abcd234@naver.com", "12345");
        Member you3 = getMember("hihihi","고양이","noise@naver.com", "111111111");
        Member you4 = getMember("testtest","유재석","jzcxhvljk@naver.com", "4444sdfwe");

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

    private Member getMember(String userId, String name, String email, String password) {
        return Member.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

}