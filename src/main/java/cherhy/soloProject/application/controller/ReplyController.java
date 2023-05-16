package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.ReplyUseCase;
import cherhy.soloProject.application.utilService.LoginService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.domain.reply.dto.response.ResponseReplyDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
    private final ReplyUseCase memberPostReplyUseCase;
    private final LoginService loginService;

    @PostMapping
    @Operation(summary = "댓글 등록")
    public ResponseEntity setReply(@RequestBody @Valid RequestReplyDto reply, Principal principal){
        Member member = loginService.getUserData(principal);
        return memberPostReplyUseCase.createReply(reply, member);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 전체")
    public List<ResponseReplyDto> getReply(@PathVariable Long postId){
        return memberPostReplyUseCase.getReply(postId);
    }

    @GetMapping("/{postId}/scroll")
    @Operation(summary = "댓글 무한 스크롤 // 날짜순 정렬 to redis")
    public ScrollResponse<ResponseReplyDto> getReplyScrollInRedis(@PathVariable Long postId, ScrollRequest scrollRequest){
        return memberPostReplyUseCase.getReplyScrollInRedis(postId, scrollRequest);
    }

}
