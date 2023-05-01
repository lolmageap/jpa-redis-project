package cherhy.soloProject.domain.member.repository;


import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
        MemberRequest member = new MemberRequest("aaaa","홍길동","abcd@naver.com","abcd");
        //when
        memberWriteService.signUp(member);
        Member findMember = memberRepository.findById(1L).get();
        //then
        Assertions.assertThat(findMember.getName()).isEqualTo("홍길동");
    }

    @Test
    public void testInsertMembers() throws Exception{
        //given
        for (int i = 0; i < 100; i++) {
            MemberRequest member = new MemberRequest("aaaa" + i,"길동" + i,"abcd"+ i +"@naver.com" ,"abcd" + i);
            memberWriteService.signUp(member);
        }
        //when
        Member findMember = memberRepository.findById(1L).get();

        //then
        Assertions.assertThat(findMember.getName()).isEqualTo("홍길동");
    }

}