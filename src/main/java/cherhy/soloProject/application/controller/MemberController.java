package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberReadService memberService;

    @GetMapping
    public String hello(){
        return "hello";
    }

    @PostMapping("/check/{email}")
    public String emailCheck(@PathVariable String email){
        memberService.emailCheck(email);
        return "";
    }

    @PostMapping("/sign/up")
    public String signUp(MemberDto memberDto){
        memberService.signUp(memberDto);
        return "";
    }



}
