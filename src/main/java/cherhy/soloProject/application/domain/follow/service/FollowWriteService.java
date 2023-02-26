package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowWriteService {

    private final FollowRepository followRepository;

    private Follow buildFollow(Member findMember, Member followMember) {
        return Follow.builder()
                .follower(findMember)
                .following(followMember)
                .build();
    }

    public void follow(Member findMember, Member followMember) {
        Follow buildFollow = buildFollow(findMember, followMember);
        followRepository.save(buildFollow);
    }

    public void unfollow(Follow follow) {
        followRepository.delete(follow);
    }

}
