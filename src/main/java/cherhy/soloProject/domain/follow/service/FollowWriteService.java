package cherhy.soloProject.domain.follow.service;

import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;

    // TODO : 팔로우하기
    public void follow(Follow follow) {
        followRepository.save(follow);
    }

    // TODO : 언팔로우하기
    public void unfollow(Follow follow) {
        followRepository.delete(follow);
    }


}
