package cherhy.soloProject.domain.member.service;


import cherhy.soloProject.domain.member.dto.request.SignInRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.exception.ExistException;
import cherhy.soloProject.exception.MemberNotFoundException;
import cherhy.soloProject.exception.PasswordNotMatchException;
import cherhy.soloProject.exception.SameMemberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Commit
class MemberReadServiceTest {

    @Autowired
    MemberReadService memberReadService;

    @Test
    @DisplayName("회원 조회")
    public void testSignup() throws Exception {
        Member member = memberReadService.getMember(1L);
        Member member2 = memberReadService.getMember("abcdef");

        assertThat(member.getName()).isEqualTo(member2.getName());
        assertThat(member.getName()).isEqualTo(member2.getName());

        Assertions.assertThrows(MemberNotFoundException.class,
                () -> memberReadService.getMember(-1L));

    }

    @Test
    @DisplayName("회원 아이디 중복체크")
    public void testUserIdCheck() throws Exception {
        assertThat(memberReadService.idCheck("notFound")
                .getBody()).isEqualTo("아이디가 사용 가능합니다");

        Assertions.assertThrows(ExistException.class,
                () -> memberReadService.idCheck("zxcvbn")
        );
    }
    @Test
    @DisplayName("이메일 중복체크")
    public void testEmailCheck() throws Exception {
        assertThat(memberReadService.emailCheck("notFound@naver.com")
                .getBody()).isEqualTo("이메일이 사용 가능합니다");

        Assertions.assertThrows(ExistException.class,
                () -> memberReadService.emailCheck("ekxk1234@gmail.com")
        );
    }

    @Test
    @DisplayName("회원 이름 검색")
    public void testSearchName() throws Exception {
        List<Member> findMembers = memberReadService.getMembers("정철희");
        List<Member> notFoundMember = memberReadService.getMembers("강호동");

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(notFoundMember.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("로그인")
    public void testSignIn() throws Exception {

        SignInRequest success = new SignInRequest("zxcvbn", "1111");
        SignInRequest idFail = new SignInRequest("fail", "1111");
        SignInRequest passwordFail = new SignInRequest("zxcvbn", "fail");

        assertThat(memberReadService.signIn(success).getBody()).isEqualTo("로그인 성공");

        Assertions.assertThrows(MemberNotFoundException.class,
                () -> memberReadService.signIn(idFail)
        );

        Assertions.assertThrows(PasswordNotMatchException.class,
                () -> memberReadService.signIn(passwordFail)
        );

    }

    @Test
    @DisplayName("자신을 팔로우, 차단 못함!")
    public void testSameMember() throws Exception {
        memberReadService.SameUserCheck(1L, 2L);

        Assertions.assertThrows(SameMemberException.class,
                () -> memberReadService.SameUserCheck(1L, 1L)
        );
    }

}