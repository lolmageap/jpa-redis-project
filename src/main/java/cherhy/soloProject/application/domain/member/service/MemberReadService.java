package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public Boolean emailCheck(String email){
        boolean res = duplicateCheckEmail(email);
        return res;
    }
    public Boolean idCheck(String userId){
        boolean res = duplicateCheckUserId(userId);
        return res;
    }
    public String signIn(SignInDto signInDto, HttpSession session){

        Member findMember = getMember(signInDto);

        if(encoder.matches(signInDto.password(), findMember.getPassword())){
            session.setAttribute("userData" , findMember);
            return findMember.getName();
        }

        return "비밀번호 오류";
    }

    private Member getMember(SignInDto signInDto) {
        return memberRepository.findByUserId(signInDto.userId()).orElseThrow(MemberNotFoundException::new);
    }

    private boolean duplicateCheckEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isEmpty()){
            return false;
        }
        return true;
    }
    private boolean duplicateCheckUserId(String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);
        if (!member.isEmpty()){
            return false;
        }
        return true;
    }

//    private void duplicateCheckEmail(String email) {
//        Optional<Member> member = memberRepository.findByEmail(email);
//        if (!member.isEmpty()){
//            throw new ExistException("email");
//        }
//    }
//    private void duplicateCheckUserId(String userId) {
//        Optional<Member> member = memberRepository.findByUserId(userId);
//        if (!member.isEmpty()){
//            throw new ExistException("id");
//        }
//    }

    public ResponseEntity<String> getResponseEmail(Boolean aBoolean) {
        if (aBoolean){
            return ResponseEntity.ok("이메일이 사용 가능합니다");
        }
        return ResponseEntity.badRequest().body("이메일이 중복됩니다");
    }
    public ResponseEntity<String> getResponseId(Boolean aBoolean) {
        if (aBoolean){
            return ResponseEntity.ok("아이디가 사용 가능합니다");
        }
        return ResponseEntity.badRequest().body("아이디가 존재합니다");
    }
}
