package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.usecase.MemberPostLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "게시물 좋아요")
@RestController
@RequiredArgsConstructor
@RequestMapping("/postLike")
public class PostLikeController {

    private final MemberPostLikeUseCase memberPostPostLikeUseCase;

    @Operation(summary = "좋아요, 취소")
    @PostMapping
    public String postLike(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = memberPostPostLikeUseCase.postLike(postLikeDto);
        return result;
    }

    @Operation(summary = "breakpoint, 좋아요 동시성 테스트")
    @PostMapping("/example")
    public String postExample(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = memberPostPostLikeUseCase.postExample(postLikeDto);
        return result;
    }

}
