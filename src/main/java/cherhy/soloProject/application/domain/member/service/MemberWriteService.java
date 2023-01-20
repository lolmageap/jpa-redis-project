package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.MemberRepository;
import cherhy.soloProject.config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public void signUp(MemberDto memberDto) throws NoSuchAlgorithmException {

        String pwd = encoder.encode(memberDto.password());

        Member a = Member.builder()
                .email(memberDto.email())
                .name(memberDto.name())
                .user_id(memberDto.user_id())
                .birthday(memberDto.birthday())
//                .password(memberDto.password())
                .password(pwd)
                .build();

        memberRepository.save(a);
    }

}
