package cherhy.soloProject.domain.member.repository;


import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.entity.Member;
import cherhy.soloProject.application.repository.jpa.MemberRepository;
import cherhy.soloProject.application.service.memberService.MemberWriteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@SpringBootTest
@Transactional
@Commit
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberWriteService memberWriteService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() throws Exception{
        //given
        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd");
        //when
        memberWriteService.signUp(member);
        Member findMember = memberRepository.findById(1L).get();
        //then
        Assertions.assertThat(findMember.getName()).isEqualTo("홍길동");
    }

}