package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.reply.dto.RequestReplyDto;
import cherhy.soloProject.application.domain.reply.service.ReplyReadService;
import cherhy.soloProject.application.domain.reply.service.ReplyWriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyWriteService replyWriteService;
    private final ReplyReadService replyReadService;

    @PostMapping
    public void setReply(@RequestBody @Valid RequestReplyDto reply){
        replyWriteService.setReply(reply);
    }

}
