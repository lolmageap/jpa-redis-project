package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.MemberFollowUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "팔로우")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final MemberFollowUseCase memberFollowUseCase;
    private final SessionReadService sessionReadService;

    @Operation(summary = "팔로우, 언팔로우")
    @PostMapping("{followingId}")
    public ResponseEntity follow(@PathVariable Long followingId, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberFollowUseCase.followMember(member, followingId);
    }

    @Operation(summary = "내 팔로우")
    @GetMapping("/following")
    public ScrollResponse<ResponseFollowMemberDto> myFollowList(ScrollRequest scrollRequest, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberFollowUseCase.getFollows(member.getId(), scrollRequest);
    }

    @Operation(summary = "내 팔로워")
    @GetMapping("/follower")
    public ScrollResponse<ResponseFollowMemberDto> myFollowerList(ScrollRequest scrollRequest, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberFollowUseCase.getFollowers(member.getId(), scrollRequest);
    }
    @Operation(summary = "상대의 팔로우")
    @GetMapping("/following/{memberId}")
    public ScrollResponse<ResponseFollowMemberDto> followList(@PathVariable("memberId") Long memberId, ScrollRequest scrollRequest){
        return memberFollowUseCase.getFollows(memberId, scrollRequest);
    }

    @Operation(summary = "상대의 팔로워")
    @GetMapping("/follower/{memberId}")
    public ScrollResponse<ResponseFollowMemberDto> followerList(@PathVariable("memberId") Long memberId, ScrollRequest scrollRequest){
        return memberFollowUseCase.getFollowers(memberId, scrollRequest);
    }

}
