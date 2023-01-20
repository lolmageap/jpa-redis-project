package cherhy.soloProject.domain.member.repository;


import org.junit.jupiter.api.Test;
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

    @Test
    public void testMember() throws Exception{
        //given

        //when

        //then
    }

}