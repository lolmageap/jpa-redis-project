package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.follow.dto.request.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.dto.response.ResponseFollowMemberDto;
import cherhy.soloProject.application.domain.follow.service.FollowReadService;
import cherhy.soloProject.application.domain.follow.service.FollowWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "팔로우")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowWriteService followWriteService;
    private final FollowReadService followReadService;

    @Operation(summary = "팔로우, 언팔로우")
    @PostMapping
    public String follow(@RequestBody @Valid FollowMemberDto followMemberDto){
        return followWriteService.followMember(followMemberDto);
    }

    @Operation(summary = "팔로우 리스트")
    @PostMapping("/followList/{id}")
    public PageScroll<ResponseFollowMemberDto> followList(@PathVariable("id") Long id, ScrollRequest scrollRequest){
        return followReadService.followList(id, scrollRequest);
    }

    @Operation(summary = "팔로워 리스트")
    @PostMapping("/followerList/{id}")
    public PageScroll<ResponseFollowMemberDto> followerList(@PathVariable("id") Long id, ScrollRequest scrollRequest){
        return followReadService.followerList(id, scrollRequest);
    }

}
