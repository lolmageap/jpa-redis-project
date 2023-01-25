package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.follow.dto.FollowMemberDto;
import cherhy.soloProject.application.domain.follow.service.FollowWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "팔로우")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowWriteService followWriteService;

    @Operation(summary = "팔로잉하기")
    @PostMapping
    public Boolean follow(FollowMemberDto followMemberDto){
        return followWriteService.followMember(followMemberDto);
    }

    @Operation(summary = "언팔하기")
    @PostMapping("/unfollow")
    public Boolean unfollow(FollowMemberDto followMemberDto){
        return followWriteService.unFollowMember(followMemberDto);
    }

}
