package cherhy.soloProject.application.controller;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.PostBlockUseCase;
import cherhy.soloProject.application.utilService.LoginService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "게시물 차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/postBlock")
public class PostBlockController {
    private final PostBlockUseCase memberPostPostBlockUseCase;
    private final LoginService loginService;

    @Operation(summary = "게시물 차단하기")
    @PostMapping("/{postId}")
    public ResponseEntity postBlock(@PathVariable Long postId, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberPostPostBlockUseCase.blockPost(member,postId);
    }

    @Operation(summary = "차단한 게시물 보기")
    @GetMapping
    public List<PostBlockResponseDto> getPostBlock(Principal principal){
        Member member = loginService.getUserData(principal);
        return memberPostPostBlockUseCase.getBlockPost(member);
    }

    @Operation(summary = "차단한 게시물 보기 cursor")
    @GetMapping("/cursor")
    public ScrollResponse getPostBlockCursor(ScrollRequest scrollRequest, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberPostPostBlockUseCase.getBlockPost(member, scrollRequest);
    }

}
