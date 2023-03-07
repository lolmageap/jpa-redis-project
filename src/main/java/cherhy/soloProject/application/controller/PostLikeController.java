package cherhy.soloProject.application.controller;

import cherhy.soloProject.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.usecase.MemberPostLikeUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity postLike(@Valid @RequestBody PostLikeDto postLikeDto){
        return memberPostPostLikeUseCase.postLike(postLikeDto);
    }

//    @Operation(summary = "좋아요 누른 게시물 확인")
//    @GetMapping
//    public ResponseEntity getPostLike(@Valid @RequestBody PostLikeDto postLikeDto){
//        return memberPostPostLikeUseCase.postLike(postLikeDto);
//    }

    @Operation(summary = "breakpoint, 좋아요 동시성 테스트")
    @PostMapping("/example")
    public ResponseEntity postExample(@Valid @RequestBody PostLikeDto postLikeDto){
        return memberPostPostLikeUseCase.postExample(postLikeDto);
    }

}
