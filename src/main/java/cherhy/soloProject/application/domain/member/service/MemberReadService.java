package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public Boolean emailCheck(String email){
        duplicateCheckEmail(email);
        return true;
    }
    public Boolean idCheck(String userId){
        duplicateCheckUserId(userId);
        return true;
    }
    public String signIn(SignInDto signInDto, HttpSession session){

        Member findMember = memberRepository.findByUserId(signInDto.userId());
        if (findMember.getId() == null){
            //login exception
            return "Id 오류";
        }

        if(encoder.matches(signInDto.password(), findMember.getPassword())){
            session.setAttribute("userData" , findMember);
            return findMember.getName();
        }
        return "비밀번호 오류";
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void duplicateCheckEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null){
            throw new NoSuchElementException("존재하는 이메일입니다.");
        }
    }
    private void duplicateCheckUserId(String userId) {
        Member member = memberRepository.findByUserId(userId);
        if (member != null){
            throw new NoSuchElementException("존재하는 아이디입니다.");
        }
    }

}
