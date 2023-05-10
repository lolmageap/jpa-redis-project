package cherhy.soloProject.application.domain.member.service;


import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class MemberWriteServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberWriteService memberWriteService;

    @Test
    @Commit
    @DisplayName("회원 가입")
    void Signup() {
        MemberRequest memberRequest = new MemberRequest("abcdef", "정철희", "ekxk1234@gmail.com", "1234");
        memberWriteService.signUp(memberRequest);

        MemberRequest memberRequest2 = new MemberRequest("qwerty", "홍길동", "abcd234@naver.com", "12345");
        memberWriteService.signUp(memberRequest2);

        MemberRequest memberRequest3 = new MemberRequest("zxcvbn", "유재석", "zxcvbn@gmail.com", "1111");
        memberWriteService.signUp(memberRequest3);
    }

    @Test
    @DisplayName("회원 정보 수정")
    void testSignup() {
        Member member = memberReadService.getMember(1L);
        MemberRequest memberRequest = new MemberRequest("abcd1234", "정철희", "ekxk1234@naver.com", "1234");
        String result = memberWriteService.modifyMember(memberRequest, member);
        assertThat(result).isEqualTo("회원정보 변경");
    }

}