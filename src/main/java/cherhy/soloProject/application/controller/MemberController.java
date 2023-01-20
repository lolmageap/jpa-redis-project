package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @GetMapping
    public String hello(){
        return "hello";
    }

    @PostMapping("/check/{email}")
    public Boolean emailCheck(@PathVariable String email){
        Boolean aBoolean = memberReadService.emailCheck(email);
        return aBoolean;
    }

    @PostMapping("/signUp")
    public String signUp(MemberDto memberDto) throws NoSuchAlgorithmException {
        memberWriteService.signUp(memberDto);
        return "";
    }

    @PostMapping("/signIn")
    public String signIn(MemberDto memberDto){
//        memberWriteService.signIn(memberDto);
        return "로그인 성공";
    }



}
