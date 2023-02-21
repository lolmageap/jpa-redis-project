package cherhy.soloProject.application.controller;


import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.postBlock.dto.request.PostBlockRequestDto;
import cherhy.soloProject.application.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockReadService;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockWriteService;
import cherhy.soloProject.application.exception.SessionNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "게시물 차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostBlockController {

    private final PostBlockWriteService postBlockWriteService;
    private final PostBlockReadService postBlockReadService;

    @Operation(summary = "게시물 차단하기")
    @PostMapping("/postBlock")
    public String postBlock(@RequestBody @Valid PostBlockRequestDto postBlockRequestDto){
        return postBlockWriteService.blockPost(postBlockRequestDto);
    }

    @Operation(summary = "차단한 게시물 보기")
    @GetMapping("/postBlockList")
    public List<PostBlockResponseDto> getPostBlock(HttpSession session){
        Member userData = getUserData(session);
        return postBlockReadService.getBlockPost(userData.getId());
    }

    private Member getUserData(HttpSession session) {
        if (session.getAttribute("userData") == null){
            throw new SessionNotFoundException();
        }
        return (Member) session.getAttribute("userData");
    }

}
