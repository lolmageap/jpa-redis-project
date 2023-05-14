package cherhy.soloProject.domain.follow.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@Transactional
@SpringBootTest
class FollowServiceTest {

    @Autowired
    FollowReadService followReadService;
    @Autowired
    FollowWriteService followWriteService;

    @BeforeEach
    @DisplayName("내가 팔로우하기")
    void followYourself() {
        Member me = new Member("test", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("testtest", "유재석", "pzkxjher@naver.com", "4321");

        Follow follow = Follow.of(me, you);
        followWriteService.follow(follow);
    }

    @BeforeEach
    @DisplayName("나를 팔로우하기")
    void followMyself() {
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");
        Member you2 = new Member("testtesttest", "강호동", "mmmmmm@naver.com", "2222");

        Follow follow1 = Follow.of(you, me);
        Follow follow2 = Follow.of(you2, me);

        followWriteService.follow(follow1);
        followWriteService.follow(follow2);
    }

    @Test
    @DisplayName("내가 팔로우하는 유저 불러오기")
    void getFollowing(){
        // given
        ScrollRequest scrollRequest = new ScrollRequest(null);
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");

        Follow follow = Follow.of(me, you);

        // when
        followWriteService.follow(follow);
        List<ResponseFollowMemberDto> follower = followReadService.getFollower(scrollRequest, me);

        // then
        assertThat(follower.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("나를 팔로잉하는 유저 불러오기")
    void getFollower(){
        // given
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");
        Member you2 = new Member("testtesttest", "강호동", "mmmmmm@naver.com", "2222");
        ScrollRequest scrollRequest = new ScrollRequest(null);

        // when
        Follow follow1 = Follow.of(you, me);
        Follow follow2 = Follow.of(you2, me);

        followWriteService.follow(follow1);
        followWriteService.follow(follow2);

        List<ResponseFollowMemberDto> follower = followReadService.getFollowing(scrollRequest, me);

        // then
        assertThat(follower.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("내가 상대방을 팔로우 하는지 체크")
    void followCheck(){
        // given
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");

        // when
        Follow follow = Follow.of(me, you);
        followWriteService.follow(follow);

        // then
        Optional<Follow> followExist = followReadService.getFollowExist(me, you);
        assertThat(followExist.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("내가 상대방을 팔로우 하는지 체크 Error")
    void followCheckError(){
        // given
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");

        // then
        assertThatThrownBy(() -> followReadService.getFollowExist(me, you).get())
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("언팔로우")
    void unfollow() {
        // given
        Member me = new Member("test1234", "정철희", "ekxk1234@naver.com", "1234");
        Member you = new Member("1234test", "유재석", "pzkxjher@naver.com", "4321");

        // when
        Follow follow = Follow.of(me, you);
        followWriteService.follow(follow);
        followWriteService.unfollow(follow);

        // then
        assertThrows(NotFoundException.class, () -> {
            followReadService.getFollowExist(me, you).orElseThrow(
                    () -> new NotFoundException("팔로워가 존재하지 않습니다.")
            );
        });

    }

}