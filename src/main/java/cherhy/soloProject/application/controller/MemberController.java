package cherhy.soloProject.application.controller;

import cherhy.soloProject.application.utilService.SessionReadService;
import cherhy.soloProject.domain.member.dto.request.MemberRequest;
import cherhy.soloProject.domain.member.dto.request.SignInRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.member.service.MemberWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.security.Principal;

@Tag(name = "회원정보")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
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
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequest memberRequestDto) {
        return memberWriteService.signUp(memberRequestDto);
    }

    @Operation(summary = "로그인")
    @GetMapping("/sign-in")
    public ResponseEntity signIn(@Valid SignInRequest signInRequestDto){
        return memberReadService.signIn(signInRequestDto);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/sign-out")
    public ResponseEntity signOut(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return ResponseEntity.ok("로그아웃 성공");
        }
        return ResponseEntity.badRequest().body(HttpStatus.BAD_REQUEST);
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping
    public String modifyMember(@Valid MemberRequest memberRequestDto, Principal principal) {
        Member member = sessionReadService.getUserData(principal);
        return memberWriteService.modifyMember(memberRequestDto, member);
    }

    private void emailValid(String email) {
        if (StringUtils.isEmpty(email) || !email.contains("@")) {
            throw new ValidationException("이메일 형식을 맞춰주세요.");
        }
    }

    @Operation(summary = "test")
    @GetMapping
    public void test(Principal principal, Authentication authentication) {
        System.out.println("authentication = " + authentication);
        System.out.println("principal = " + principal);
    }

}
