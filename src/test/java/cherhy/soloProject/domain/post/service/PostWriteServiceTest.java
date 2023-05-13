package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import cherhy.soloProject.domain.member.service.MemberWriteServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PostWriteServiceTest {

    @Autowired
    PostWriteService postWriteService;
    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberReadService memberReadService;

    @BeforeEach
    @DisplayName("회원 추가")
    void addMember(){
        MemberWriteServiceTest memberWriteServiceTest = new MemberWriteServiceTest(memberReadService, memberWriteService);
        memberWriteServiceTest.addMember();
    }

    @Test
    @DisplayName("게시글 등록")
    void testUnblock(){
        // given
        Member member = memberReadService.getMember(1L);

        // when
        Assertions.assertThat(member.getId()).isEqualTo(1L);
        // then

    }

}