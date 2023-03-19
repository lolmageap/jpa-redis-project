package cherhy.soloProject.application.utilService;

import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.application.exception.SessionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionReadService {

    // TODO : 로그인 확인
    public Long getUserData(HttpSession session) {
        if (session.getAttribute("userData") == null){
            throw new SessionNotFoundException();
        }
        Member userData = (Member) session.getAttribute("userData");
        return userData.getId();
    }

    // TODO : 로그인 확인, Exception 던지지않음!
    public Long getUserDataNoThrow(HttpSession session) {
        if (session.getAttribute("userData") == null){
            return null;
        }
        Member userData = (Member) session.getAttribute("userData");
        return userData.getId();
    }

    // TODO : 로그아웃
    public ResponseEntity signOut(HttpSession session) {
        if (session.getAttribute("userData") == null){
            throw new SessionNotFoundException();
        }
        session.removeAttribute("userData");
        return ResponseEntity.ok("로그아웃");
    }
}
