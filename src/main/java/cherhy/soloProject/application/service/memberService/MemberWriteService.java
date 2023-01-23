package cherhy.soloProject.application.service.memberService;

import cherhy.soloProject.application.domain.dto.MemberDto;
import cherhy.soloProject.application.domain.dto.SignInDto;
import cherhy.soloProject.application.domain.entity.Member;
import cherhy.soloProject.application.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public void signUp(MemberDto memberDto) {

        String pwd = encoder.encode(memberDto.password());
        Member a = Member.builder()
                .email(memberDto.email())
                .name(memberDto.name())
                .user_id(memberDto.user_id())
//                .birthday(memberDto.birthday())
                .password(pwd)
                .build();
        memberRepository.save(a);
    }

    public String signIn(SignInDto signInDto, HttpSession session){

        Member findMember = memberRepository.findByUserId(signInDto.user_Id());
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

}
