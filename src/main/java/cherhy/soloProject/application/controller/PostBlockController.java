package cherhy.soloProject.application.controller;


import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.MemberPostBlockUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Tag(name = "게시물 차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/postBlock")
public class PostBlockController {
    private final HttpSession session;
    private final MemberPostBlockUseCase memberPostPostBlockUseCase;
    private final SessionReadService sessionReadService;

    @Operation(summary = "게시물 차단하기")
    @PostMapping("/{postId}")
    public ResponseEntity postBlock(@PathVariable Long postId){
        Long memberId = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.blockPost(memberId,postId);
    }

    @Operation(summary = "차단한 게시물 보기")
    @GetMapping
    public List<PostBlockResponseDto> getPostBlock(){
        Long memberId = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.getBlockPost(memberId);
    }

    @Operation(summary = "차단한 게시물 보기 cursor")
    @GetMapping("/cursor")
    public ScrollResponse getPostBlockCursor(ScrollRequest scrollRequest){
        Long memberId = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.getBlockPost(memberId, scrollRequest);
    }

}
