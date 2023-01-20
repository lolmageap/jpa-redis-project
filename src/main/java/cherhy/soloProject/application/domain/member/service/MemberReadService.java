package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.MemberRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;

    public Boolean emailCheck(String email){
        Member member = memberRepository.findByEmail(email);

        if (member != null){
            throw new IllegalStateException("존재하는 이메일입니다.");
        }
        return true;
    }

    public void signUp(MemberDto memberDto){
        Member member = Member.builder()
                .build();
    }

}
