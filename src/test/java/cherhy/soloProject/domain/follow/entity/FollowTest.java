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
        Member me = getMember("abcdef","정철희","ekxk1234@gmail.com", "1234");

        Member you = getMember("test1234","유재석","ekxk1234@naver.com", "4321");

        //when
        Follow follow = Follow.of(me, you);

        //then
        Assertions.assertThat(follow.getFollower()).isSameAs(me);
        Assertions.assertThat(follow.getFollowing()).isSameAs(you);
    }

    private Member getMember(String userId, String name, String email, String password) {
        return Member.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

}