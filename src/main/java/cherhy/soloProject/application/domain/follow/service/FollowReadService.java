package cherhy.soloProject.application.domain.follow.service;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.domain.follow.repository.jpa.FollowRepository;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    public PageScroll<ResponseFollowMemberDto> followList(Long id, ScrollRequest scrollRequest) {
        Member member = getMember(id);
        List<ResponseFollowMemberDto> getFollowing = followRepository.findAllByFollowing(member, scrollRequest);
        return getResponseFollowerMemberDtoPageScroll(getFollowing, scrollRequest);
    }

    public PageScroll<ResponseFollowMemberDto> followerList(Long id, ScrollRequest scrollRequest) {
        Member member = getMember(id);
        List<ResponseFollowMemberDto> getFollow = followRepository.findAllByFollower(member, scrollRequest);
        return getResponseFollowerMemberDtoPageScroll(getFollow, scrollRequest);
    }

    private PageScroll<ResponseFollowMemberDto> getResponseFollowerMemberDtoPageScroll
            (List<ResponseFollowMemberDto> follow, ScrollRequest scrollRequest) {
        List<ResponseFollowMemberDto> resMemberDto = follow;
        long nextKey = getNextKey(scrollRequest, resMemberDto);
        return new PageScroll<>(scrollRequest.next(nextKey), resMemberDto);
    }

    private Member getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        return member;
    }

    private long getNextKey(ScrollRequest scrollRequest, List<ResponseFollowMemberDto> resMemberDto) {
        return resMemberDto.stream().mapToLong(m -> m.followId()).min().orElse(scrollRequest.NONE_KEY);
    }

}
