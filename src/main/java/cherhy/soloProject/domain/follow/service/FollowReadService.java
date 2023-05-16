package cherhy.soloProject.domain.follow.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.FollowMemberResponse;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowRepository followRepository;

    // TODO : 무한스크롤 팔로잉 정보 조회
    public List<FollowMemberResponse> getFollowing(ScrollRequest scrollRequest, Member member) {
        return followRepository.findAllByFollowing(member, scrollRequest);
    }

    // TODO : 무한스크롤 팔로워 정보 조회
    public List<FollowMemberResponse> getFollower(ScrollRequest scrollRequest, Member member) {
        return followRepository.findAllByFollower(member, scrollRequest);
    }

    // TODO : 팔로우 하는지 체크
    public Optional<Follow> getFollowExist(Member myMember, Member followMember) {
        return followRepository.followCheck(myMember.getId(), followMember.getId());
    }

    // TODO : 무한스크롤 팔로우 다음 키 체크
    public long getNextKey(ScrollRequest scrollRequest, List<FollowMemberResponse> resMemberDto) {
        return resMemberDto.stream().mapToLong(m -> m.followId()).min().orElse(scrollRequest.NONE_KEY);
    }

    public List<Member> findAllByFollowers(Member findMember) {
        return followRepository.findAllByFollowers(findMember.getId()).orElseThrow(MemberNotFoundException::new);
    }

}
