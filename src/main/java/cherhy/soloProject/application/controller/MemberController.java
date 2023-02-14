package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.member.service.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @Operation(summary = "이메일 체크")
    @GetMapping("/check/email")
    public ResponseEntity<String> emailCheck(@Email @RequestParam("email") String email){

        if (StringUtils.isEmpty(email) || !email.contains("@")) {
            return ResponseEntity.badRequest().body("이메일 형식을 맞춰주세요");
        }

        Boolean aBoolean = memberReadService.emailCheck(email);
        ResponseEntity<String> responseEmail = memberReadService.getResponseEmail(aBoolean);
        return responseEmail;
    }


    @Operation(summary = "id 체크")
    @GetMapping("/check/id/{userId}")
    public ResponseEntity<String> idCheck(@PathVariable @NotBlank String userId){
        Boolean aBoolean = memberReadService.idCheck(userId);
        ResponseEntity<String> responseEmail = memberReadService.getResponseId(aBoolean);
        return responseEmail;
    }


    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    public String signUp(@RequestBody @Valid MemberDto memberDto) {
        return memberWriteService.signUp(memberDto);
    }

    @Operation(summary = "로그인")
    @GetMapping("/signIn")
    public String signIn(@Valid SignInDto signInDto, HttpSession session){
        return memberReadService.signIn(signInDto, session);
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/signUp/{memberId}")
    public String modifyMember(@RequestBody @Valid MemberDto memberDto , @PathVariable Long memberId) {
        return memberWriteService.modifyMember(memberDto, memberId);
    }

}
