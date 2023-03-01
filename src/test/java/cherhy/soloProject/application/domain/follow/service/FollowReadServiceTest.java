package cherhy.soloProject.domain.follow.service;

import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.repository.jpa.FollowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FollowReadServiceTest {

    @Autowired
    FollowRepository followRepository;

    @Test
    public void test(){
        Optional<Follow> follow = followRepository.followCheck(1L, 3L);
        assertThat(follow.get().getId()).isEqualTo(2);
    }

}