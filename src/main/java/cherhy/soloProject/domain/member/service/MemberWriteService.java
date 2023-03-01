package cherhy.soloProject.domain.member.service;

import cherhy.soloProject.domain.member.dto.request.MemberRequestDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public ResponseEntity signUp(MemberRequestDto memberRequestDto) {
            Member member = BuildMember(memberRequestDto);
            member.changePassword(encoder.encode(memberRequestDto.password()));
            memberRepository.save(member);
        return ResponseEntity.ok(200);
    }

    public String modifyMember(MemberRequestDto memberRequestDto, Long memberId) {
        Member findMember = getMember(memberId);
        memberRepository.save(modify(memberRequestDto, findMember));
        return "회원정보 변경";
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member modify(MemberRequestDto memberRequestDto, Member findMember) {
        if (!memberRequestDto.user_id().isEmpty()){
            findMember.changeUserId(memberRequestDto.user_id());
        }
        if (!memberRequestDto.name().isEmpty()){
            findMember.changeName(memberRequestDto.name());
        }
        if (!memberRequestDto.password().isEmpty()){
            findMember.changePassword(encoder.encode(memberRequestDto.password()));
        }
        return findMember;
    }

    private Member BuildMember(MemberRequestDto memberRequestDto) {
        Member member = Member.builder()
                .email(memberRequestDto.email())
                .name(memberRequestDto.name())
                .userId(memberRequestDto.user_id())
                .password(memberRequestDto.password())
                .build();
        return member;
    }


}
