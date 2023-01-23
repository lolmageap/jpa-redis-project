package cherhy.soloProject.application.service.memberService;

import cherhy.soloProject.application.domain.entity.Member;
import cherhy.soloProject.application.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public Boolean emailCheck(String email){
        duplicateCheckEmail(email);
        return true;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void duplicateCheckEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null){
            throw new IllegalStateException("존재하는 이메일입니다.");
        }
    }
}
