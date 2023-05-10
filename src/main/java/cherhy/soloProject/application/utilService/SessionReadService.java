package cherhy.soloProject.application.utilService;

import cherhy.soloProject.exception.MemberNotFoundException;
import cherhy.soloProject.exception.SessionNotFoundException;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionReadService {

    private final MemberRepository memberRepository;

    // TODO : 로그인 확인
    public Member getUserData(Principal principal) {
        String userId = principal.getName();
        if (userId == null){
            throw new SessionNotFoundException();
        }
        return memberRepository.findByUserId(userId).orElseThrow(() -> new MemberNotFoundException());
    }

    // TODO : 로그인 확인, Exception 던지지않음!
    public Member getUserDataNoThrow(Principal principal) {
        String userId = principal.getName();
        if (userId == null){
            return null;
        }
        return memberRepository.findByUserId(userId).orElseThrow(() -> new MemberNotFoundException());
    }

}
