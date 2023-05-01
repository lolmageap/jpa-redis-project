package cherhy.soloProject.application.domain.member.service;


import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;



@SpringBootTest
@Commit
class MemberReadServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Test
    public void testSignup() throws Exception{
        memberWriteService.signUp(new MemberRequest("abcdef", "정철희", "ekxk1234@gmail.com", "ss012012"));
    }

}