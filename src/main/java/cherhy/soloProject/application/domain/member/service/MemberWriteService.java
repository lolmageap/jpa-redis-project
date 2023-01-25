package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.MemberDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public String signUp(MemberDto memberDto) {
            Member member = BuildMember(memberDto);
            member.changePassword(encoder.encode(memberDto.password()));
            memberRepository.save(member);
        return "회원가입";
    }

    public String modifyMember(MemberDto memberDto, Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(NoSuchElementException::new);

        memberRepository.save(modify(memberDto, findMember));
        return "회원정보 변경";
    }

    private Member modify(MemberDto memberDto, Member findMember) {
        if (!memberDto.user_id().isEmpty()){
            findMember.validUserId(memberDto.user_id());
            findMember.changeUserId(memberDto.user_id());
        }

        if (!memberDto.name().isEmpty()){
            findMember.validName(memberDto.name());
            findMember.changeName(memberDto.name());
        }

        if (!memberDto.password().isEmpty()){
            findMember.validPwd(memberDto.password());
            findMember.changePassword(encoder.encode(memberDto.password()));
        }
        return findMember;
    }

    private Member BuildMember(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.email())
                .name(memberDto.name())
                .user_id(memberDto.user_id())
                .password(memberDto.password())
                .build();
        return member;
    }


}
