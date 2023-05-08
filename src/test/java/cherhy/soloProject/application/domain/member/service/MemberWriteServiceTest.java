package cherhy.soloProject.application.domain.member.service;


import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class MemberWriteServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberWriteService memberWriteService;

    @Test
    @DisplayName("회원 가입")
    void Signup() {
        MemberRequest memberRequest = new MemberRequest("abcdef", "정철희", "ekxk1234@gmail.com", "1234");
        memberWriteService.signUp(memberRequest);
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