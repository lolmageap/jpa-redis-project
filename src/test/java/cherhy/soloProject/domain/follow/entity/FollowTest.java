package cherhy.soloProject.domain.follow.entity;

import cherhy.soloProject.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class FollowTest {

    @Test
    @DisplayName("팔로우, 팔로워 확인합니다.")
    void test(){
        //given
        Member me = Member.builder()
                .userId("abcdef")
                .name("정철희")
                .email("ekxk1234@gmail.com")
                .password("1234")
                .build();

        Member you = Member.builder()
                .userId("test1234")
                .name("유재석")
                .email("ekxk1234@naver.com")
                .password("4321")
                .build();

        //when
        Follow follow = Follow.of(me, you);

        //then
        Assertions.assertThat(follow.getFollower()).isSameAs(me);
        Assertions.assertThat(follow.getFollowing()).isSameAs(you);
    }

}