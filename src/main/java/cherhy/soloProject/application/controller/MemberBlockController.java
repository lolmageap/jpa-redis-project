package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.usecase.MemberBlockUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "회원 차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/memberBlock")
public class MemberBlockController {

    private final SessionReadService sessionReadService;
    private final MemberBlockUseCase memberBlockUseCase;

    @Operation(summary = "회원 차단하기")
    @PostMapping("/{blockMemberId}")
    public ResponseEntity blockMember(@PathVariable Long blockMemberId, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberBlockUseCase.blockMember(userData.getId(), blockMemberId);
    }

    @Operation(summary = "차단한 회원 보기")
    @GetMapping("/blockList")
    public PageScroll getBlockMember(ScrollRequest scrollRequest, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        return memberBlockUseCase.getBlockMember(userData.getId(),scrollRequest);
    }

}
