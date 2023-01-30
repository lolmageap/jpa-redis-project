package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.application.domain.reply.dto.response.ResponseReplyDto;
import cherhy.soloProject.application.domain.reply.service.ReplyReadService;
import cherhy.soloProject.application.domain.reply.service.ReplyWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyWriteService replyWriteService;
    private final ReplyReadService replyReadService;

    @PostMapping
    @Operation(summary = "댓글 등록")
    public Boolean setReply(@RequestBody @Valid RequestReplyDto reply){
        return replyWriteService.setReply(reply);
    }

    @GetMapping("/{postId}")
    @Operation(summary = "댓글 전체")
    public List<ResponseReplyDto> getReply(@PathVariable Long postId){
        return replyReadService.getReply(postId);
    }

    @GetMapping("/{postId}/scroll")
    @Operation(summary = "댓글 무한 스크롤 // 날짜순 정렬 to redis")
    public PageScroll<ResponseReplyDto> getReplyScrollInRedis(@PathVariable Long postId, ScrollRequest scrollRequest){
        return replyReadService.getReplyScrollInRedis(postId, scrollRequest);
    }


}
