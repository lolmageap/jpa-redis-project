package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.application.usecase.MemberBlockUseCase;
import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Tag(name = "회원 차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/memberBlock")
public class MemberBlockController {
    private final SessionReadService sessionReadService;
    private final MemberBlockUseCase memberBlockUseCase;

    @Operation(summary = "회원 차단하기")
    @PostMapping("/{blockMemberId}")
    public ResponseEntity blockMember(@PathVariable Long blockMemberId, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberBlockUseCase.blockMember(member, blockMemberId);
    }

    @Operation(summary = "차단한 회원 보기")
    @GetMapping
    public ScrollResponse getBlockMember(ScrollRequest scrollRequest, Principal principal){
        Member member = sessionReadService.getUserData(principal);
        return memberBlockUseCase.getBlockMember(member,scrollRequest);
    }

//    @Operation(summary = "차단한 회원 보기")
//    @GetMapping
//    public List<Long> getBlockMember(){
//        Long memberId = sessionReadService.getUserData(session);
//        return memberBlockUseCase.getBlockMember(memberId);
//    }

}
