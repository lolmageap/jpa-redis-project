package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @Operation(summary = "이메일 체크")
    @PostMapping("/check/{email}")
    public Boolean emailCheck(@PathVariable String email){
        Boolean aBoolean = memberReadService.emailCheck(email);
        return aBoolean;
    }

    @Operation(summary = "id 체크")
    @PostMapping("/check/{userId}")
    public Boolean idCheck(@PathVariable String userId){
        Boolean aBoolean = memberReadService.idCheck(userId);
        return aBoolean;
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    public String signUp(MemberDto memberDto) {
        return memberWriteService.signUp(memberDto);
    }

    @Operation(summary = "로그인")
    @GetMapping("/signIn")
    public String signIn(SignInDto signInDto, HttpSession session){
        return memberReadService.signIn(signInDto, session);
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/signUp/{memberId}")
    public String modifyMember(@PathVariable Long memberId, MemberDto memberDto) {
        return memberWriteService.modifyMember(memberDto, memberId);
    }

}
