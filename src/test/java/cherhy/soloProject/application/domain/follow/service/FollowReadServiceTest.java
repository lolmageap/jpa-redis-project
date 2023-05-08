package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.follow.service.FollowWriteService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Rollback
@SpringBootTest
class FollowReadServiceTest {

    @Autowired
    MemberReadService memberReadService;
    @Autowired
    FollowReadService followReadService;
    @Autowired
    FollowWriteService followWriteService;

    @BeforeEach
    @DisplayName("내가 팔로우하기")
    void followYourself() {
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(3L);
        followWriteService.follow(me, you);
    }

    @BeforeEach
    @DisplayName("나를 팔로우하기")
    void followMyself() {
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(2L);
        Member you2 = memberReadService.getMember(3L);

        followWriteService.follow(you, me);
        followWriteService.follow(you2, me);
    }

    @Test
    @DisplayName("나를 팔로우하는 유저 불러오기")
    void getFollower(){
        Member member = memberReadService.getMember(1L);
        ScrollRequest scrollRequest = new ScrollRequest(null);
        List<ResponseFollowMemberDto> follower = followReadService.getFollower(scrollRequest, member);
        assertThat(follower.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("내가 팔로잉하는 유저 불러오기")
    void getFollowing(){
        Member member = memberReadService.getMember(1L);
        ScrollRequest scrollRequest = new ScrollRequest(null);
        List<ResponseFollowMemberDto> follower = followReadService.getFollowing(scrollRequest, member);
        assertThat(follower.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("내가 상대방을 팔로우 하는지 체크")
    void followCheck(){
        Member me = memberReadService.getMember(1L);
        Member you = memberReadService.getMember(3L);

        Optional<Follow> follow = followReadService.getFollowExist(me, you);
        assertThat(follow.get().getId()).isEqualTo(2);
    }

}