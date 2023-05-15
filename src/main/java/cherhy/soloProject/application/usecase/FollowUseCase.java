package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.domain.follow.entity.Follow;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.follow.service.FollowWriteService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FollowUseCase {

    private final MemberReadService memberReadService;
    private final FollowWriteService followWriteService;
    private final FollowReadService followReadService;
    private final MemberBlockReadService memberBlockReadService;
    private final MemberBlockWriteService memberBlockWriteService;

    public ResponseEntity followMember(Member member, Long FollowingId){
        memberReadService.SameUserCheck(member.getId(), FollowingId);
        Member myMember = memberReadService.getMember(member.getId());
        Member followMember = memberReadService.getMember(FollowingId);
        Optional<Follow> follow = followReadService.getFollowExist(myMember, followMember);

        follow.ifPresentOrElse(f -> followWriteService.unfollow(f),
                () -> { memberBlockReadService.getBlockMember(myMember,followMember)
                            .ifPresent(m -> memberBlockWriteService.unblock(m));
                        followWriteService.follow(Follow.of(myMember, followMember));
        });

        return ResponseEntity.ok("성공");
    }
    @Cacheable(cacheNames = "followList", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse<ResponseFollowMemberDto> getFollows(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<ResponseFollowMemberDto> follow = followReadService.getFollowing(scrollRequest, member);
        long nextKey = followReadService.getNextKey(scrollRequest, follow);
        return new ScrollResponse<>(scrollRequest.next(nextKey), follow);
    }
    @Cacheable(cacheNames = "followerList", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse<ResponseFollowMemberDto> getFollowers(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<ResponseFollowMemberDto> follow = followReadService.getFollower(scrollRequest, member);
        long nextKey = followReadService.getNextKey(scrollRequest, follow);
        return new ScrollResponse<>(scrollRequest.next(nextKey), follow);
    }

}
