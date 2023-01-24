package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.dto.SignInDto;
import cherhy.soloProject.application.service.memberService.MemberReadService;
import cherhy.soloProject.application.service.memberService.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    public String signUp(MemberDto memberDto) {
        memberWriteService.signUp(memberDto);
        return "";
    }

    @Operation(summary = "로그인")
    @GetMapping("/signIn")
    public String signIn(SignInDto signInDto, HttpSession session){
        return memberWriteService.signIn(signInDto, session);
    } 

}
