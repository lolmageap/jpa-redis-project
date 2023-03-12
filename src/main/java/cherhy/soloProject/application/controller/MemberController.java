package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.member.dto.request.MemberRequestDto;
import cherhy.soloProject.domain.member.dto.request.SignInRequestDto;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final HttpSession session;
    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;
    private final SessionReadService sessionReadService;
    @Operation(summary = "이메일 체크")
    @GetMapping("/check/email")
    public ResponseEntity emailCheck(@Email @RequestParam("email") String email){
        emailValid(email);
        return memberReadService.emailCheck(email);
    }
    @Operation(summary = "id 체크")
    @GetMapping("/check/{userId}")
    public ResponseEntity idCheck(@PathVariable @NotBlank String userId){
        return memberReadService.idCheck(userId);
    }
    @Operation(summary = "회원가입")
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return memberWriteService.signUp(memberRequestDto);
    }
    @Operation(summary = "로그인")
    @GetMapping("/signIn")
    public ResponseEntity signIn(@Valid @RequestBody SignInRequestDto signInRequestDto){
        return memberReadService.signIn(signInRequestDto, session);
    }
    @Operation(summary = "시큐리티 로그인 연습")
    @GetMapping("/signIn/exam")
    public String signInExam(Authentication authentication){

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        }
        return "";
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/signOut")
    public ResponseEntity signOut(){
        return sessionReadService.signOut(session);
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping
    public String modifyMember(@Valid MemberRequestDto memberRequestDto) {
        Long memberId = sessionReadService.getUserData(session);
        return memberWriteService.modifyMember(memberRequestDto, memberId);
    }

    private void emailValid(String email) {
        if (StringUtils.isEmpty(email) || !email.contains("@")) {
            throw new ValidationException("이메일 형식을 맞춰주세요.");
        }
    }

}
