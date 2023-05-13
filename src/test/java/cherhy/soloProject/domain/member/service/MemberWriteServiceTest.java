package cherhy.soloProject.domain.member.service;


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
public class MemberWriteServiceTest {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    public MemberWriteServiceTest(MemberReadService memberReadService, MemberWriteService memberWriteService) {
        this.memberReadService = memberReadService;
        this.memberWriteService = memberWriteService;
    }

    @Test
    @Commit
    @DisplayName("회원 가입")
    void Signup() {
        addMember();
    }

    public void addMember() {
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
    @DisplayName("회원 정보 수정")
    void testSignup() {
        Member member = memberReadService.getMember(1L);
        MemberRequest memberRequest = new MemberRequest("abcd1234", "정철희", "ekxk1234@naver.com", "1234");
        String result = memberWriteService.modifyMember(memberRequest, member);
        assertThat(result).isEqualTo("회원정보 변경");
    }

}