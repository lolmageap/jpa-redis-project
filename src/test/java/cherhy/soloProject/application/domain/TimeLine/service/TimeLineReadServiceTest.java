package cherhy.soloProject.application.domain.TimeLine.service;

import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class TimeLineReadServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void test(){
        List<Member> allByFollowers = memberRepository.findAllByFollowers(18L).orElseThrow(() -> new MemberNotFoundException());
        for (Member allByFollower : allByFollowers) {
            System.out.println("allByFollower.getId() = " + allByFollower.getId());
        }
        Assertions.assertThat(allByFollowers.size()).isEqualTo(2);
    }


}