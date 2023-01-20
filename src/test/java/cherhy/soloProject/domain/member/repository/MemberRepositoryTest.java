package cherhy.soloProject.domain.member.repository;


import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
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

    @Test
    public void testMember() throws Exception{
        //given
        MemberDto member = new MemberDto("aaaa","김병기","abcd@naver.com","asdfasdfasdf", LocalDate.now());

        //when
        memberWriteService.signUp(member);

        //then
    }

}