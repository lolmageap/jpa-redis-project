package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.request.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.follow.service.FollowReadService;
import cherhy.soloProject.application.domain.follow.service.FollowWriteService;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberFollowUseCase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;
    private final FollowReadService followReadService;

    public ResponseEntity followMember(FollowMemberDto followMemberDto){
        Member findMember = memberReadService.getMember(followMemberDto.MemberId());
        Member followMember = memberReadService.getMember(followMemberDto.FollowerId());

        Optional<Follow> follow = followReadService.getFollowExist(findMember, followMember);

        follow.ifPresentOrElse(f -> followWriteService.unfollow(f),
                () -> followWriteService.follow(findMember, followMember));

        return ResponseEntity.ok(200);
    }

    public ScrollResponse<ResponseFollowMemberDto> followList(Long id, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(id);
        List<ResponseFollowMemberDto> getFollowing = followReadService.getFollowing(scrollRequest, member);
        return followReadService.getResponseFollowerMemberDtoScroll(getFollowing, scrollRequest);
    }

    public ScrollResponse<ResponseFollowMemberDto> followerList(Long id, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(id);
        List<ResponseFollowMemberDto> getFollow = followReadService.getFollower(scrollRequest, member);
        return followReadService.getResponseFollowerMemberDtoScroll(getFollow, scrollRequest);
    }

}
