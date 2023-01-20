package cherhy.soloProject.domain.member.repository;


import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.MemberRepository;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

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
        MemberDto member = new MemberDto("aaaa","홍길동","abcd@naver.com","abcd", LocalDate.now());
        //when
        memberWriteService.signUp(member);
        Member findMember = memberRepository.findById(1L).get();
        //then
        assertThat(findMember.getName()).isEqualTo("홍길동");
    }

}