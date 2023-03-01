package cherhy.soloProject.domain.follow.service;

import cherhy.soloProject.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.domain.follow.service.FollowWriteService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.usecase.MemberFollowUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Commit
class FollowWriteServiceTest {

    @Autowired
    FollowWriteService followWriteService;
    MemberFollowUseCase memberFollowUseCase;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() throws Exception{
        Member byId = memberRepository.findById(1L).get();
        assertThat(byId.getName()).isEqualTo("홍길동");
    }

    @Test
    @Rollback(value = false)
    public void testFollow() throws Exception{
//        //given
//        FollowMemberDto followMemberDto = new FollowMemberDto(1L, 2L);
//        //when
//        memberFollowUseCase.followMember(followMemberDto);
//
//        List<Follow> all = followRepository.findAll();
//        //then
//        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    @Rollback(value = false)
    public void testUnfollow(){


    }

}