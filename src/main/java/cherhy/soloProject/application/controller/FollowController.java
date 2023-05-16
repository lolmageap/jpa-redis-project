package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.FollowUseCase;
import cherhy.soloProject.application.utilService.LoginService;
import cherhy.soloProject.domain.follow.dto.response.FollowMemberResponse;
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
    private final FollowUseCase memberFollowUseCase;
    private final LoginService loginService;

    @Operation(summary = "팔로우, 언팔로우")
    @PostMapping("{followingId}")
    public ResponseEntity follow(@PathVariable Long followingId, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberFollowUseCase.followMember(member, followingId);
    }

    @Operation(summary = "내 팔로우")
    @GetMapping("/following")
    public ScrollResponse<FollowMemberResponse> myFollowList(ScrollRequest scrollRequest, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberFollowUseCase.getFollows(member.getId(), scrollRequest);
    }

    @Operation(summary = "내 팔로워")
    @GetMapping("/follower")
    public ScrollResponse<FollowMemberResponse> myFollowerList(ScrollRequest scrollRequest, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberFollowUseCase.getFollowers(member.getId(), scrollRequest);
    }
    @Operation(summary = "상대의 팔로우")
    @GetMapping("/following/{memberId}")
    public ScrollResponse<FollowMemberResponse> followList(@PathVariable("memberId") Long memberId, ScrollRequest scrollRequest){
        return memberFollowUseCase.getFollows(memberId, scrollRequest);
    }

    @Operation(summary = "상대의 팔로워")
    @GetMapping("/follower/{memberId}")
    public ScrollResponse<FollowMemberResponse> followerList(@PathVariable("memberId") Long memberId, ScrollRequest scrollRequest){
        return memberFollowUseCase.getFollowers(memberId, scrollRequest);
    }

}
