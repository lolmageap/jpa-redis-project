package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
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

    public List<ResponseFollowMemberDto> getFollowing(ScrollRequest scrollRequest, Member member) {
        return followRepository.findAllByFollowing(member, scrollRequest);
    }

    public List<ResponseFollowMemberDto> getFollower(ScrollRequest scrollRequest, Member member) {
        return followRepository.findAllByFollower(member, scrollRequest);
    }

    public Optional<Follow> getFollowExist(Member myMember, Member followMember) {
        return followRepository.followCheck(myMember.getId(), followMember.getId());
    }

    public ScrollResponse<ResponseFollowMemberDto> getResponseFollowerMemberDtoScroll(List<ResponseFollowMemberDto> follow, ScrollRequest scrollRequest) {
        List<ResponseFollowMemberDto> resMemberDto = follow;
        long nextKey = getNextKey(scrollRequest, resMemberDto);
        return new ScrollResponse<>(scrollRequest.next(nextKey), resMemberDto);
    }

    private long getNextKey(ScrollRequest scrollRequest, List<ResponseFollowMemberDto> resMemberDto) {
        return resMemberDto.stream().mapToLong(m -> m.followId()).min().orElse(scrollRequest.NONE_KEY);
    }

}
