package cherhy.soloProject.application.controller;


import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.usecase.MemberPostBlockUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
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

    private final MemberPostBlockUseCase memberPostPostBlockUseCase;
    private final SessionReadService sessionReadService;

    @Operation(summary = "게시물 차단하기")
    @PostMapping("/{postId}")
    public ResponseEntity postBlock(@PathVariable Long postId, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.blockPost(userData.getId(),postId);
    }

    @Operation(summary = "차단한 게시물 보기")
    @GetMapping("/postBlockList")
    public List<PostBlockResponseDto> getPostBlock(HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.getBlockPost(userData.getId());
    }

    @Operation(summary = "차단한 게시물 보기 cursor")
    @GetMapping("/postBlockList/cursor")
    public ScrollResponse getPostBlockCursor(ScrollRequest scrollRequest, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberPostPostBlockUseCase.getBlockPost(userData.getId(), scrollRequest);
    }

}
