package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.PostLikeUseCase;
import cherhy.soloProject.application.utilService.LoginService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postLike.dto.request.PostLikeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Tag(name = "게시물 좋아요")
@RestController
@RequiredArgsConstructor
@RequestMapping("/postLike")
public class PostLikeController {
    private final PostLikeUseCase memberPostPostLikeUseCase;
    private final LoginService loginService;

    @Operation(summary = "좋아요, 취소")
    @PostMapping
    public ResponseEntity postLike(@Valid @RequestBody PostLikeRequest postLikeDto){
        return memberPostPostLikeUseCase.postLike(postLikeDto);
    }

    @Operation(summary = "좋아요 누른 게시물 확인")
    @GetMapping
    public ScrollResponse getPostLike(ScrollRequest scrollRequest, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberPostPostLikeUseCase.getPostLike(member, scrollRequest);
    }

}
