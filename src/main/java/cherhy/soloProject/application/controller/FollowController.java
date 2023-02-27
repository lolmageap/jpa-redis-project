package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.request.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.usecase.MemberFollowUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "팔로우")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final MemberFollowUseCase memberFollowUseCase;

    @Operation(summary = "팔로우, 언팔로우")
    @PostMapping
    public ResponseEntity follow(@RequestBody @Valid FollowMemberDto followMemberDto){
        return memberFollowUseCase.followMember(followMemberDto);
    }

    @Operation(summary = "팔로우 리스트")
    @GetMapping("/followList/{id}")
    public ScrollResponse<ResponseFollowMemberDto> followList(@PathVariable("id") Long id, ScrollRequest scrollRequest){
        return memberFollowUseCase.followList(id, scrollRequest);
    }

    @Operation(summary = "팔로워 리스트")
    @GetMapping("/followerList/{id}")
    public ScrollResponse<ResponseFollowMemberDto> followerList(@PathVariable("id") Long id, ScrollRequest scrollRequest){
        return memberFollowUseCase.followerList(id, scrollRequest);
    }

}
