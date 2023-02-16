package cherhy.soloProject.application.domain.member.service;


import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.dto.MemberSearchDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Commit
class MemberReadServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    MemberWriteService memberWriteService;
    @Test
    public void testSignup() throws Exception{
        memberWriteService.signUp(new MemberDto("abcdef", "정철희", "ekxk1234@gmail.com", "ss012012"));
    }

    @Test
    public void testMember() throws Exception{
        List<MemberSearchDto> find = memberReadService.searchMember(new MemberSearchDto(1L,"정철희"));
        assertThat(find.size()).isEqualTo(2);
    }

}