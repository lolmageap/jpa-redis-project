package cherhy.soloProject.domain.member.service;

import cherhy.soloProject.domain.member.dto.request.MemberRequestDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static cherhy.soloProject.application.key.RedisKey.SEARCH_LOG;
import static cherhy.soloProject.application.key.RedisKey.SEARCH_RANK;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberReadService memberReadService;
    private final PasswordEncoder encoder;

    // TODO : 회원가입
    public ResponseEntity signUp(MemberRequestDto memberRequestDto) {
            Member member = BuildMember(memberRequestDto);
            member.changePassword(encoder.encode(memberRequestDto.password()));
            memberRepository.save(member);
        return ResponseEntity.ok("회원가입 완료");
    }

    // TODO : 회원 정보 수정
    public String modifyMember(MemberRequestDto memberRequestDto, Long memberId) {
        Member findMember = memberReadService.getMember(memberId);
        memberRepository.save(modify(memberRequestDto, findMember));
        return "회원정보 변경";
    }
    

    // TODO : 정보 수정 전 validation check
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

    // TODO : 회원 빌드
    private Member BuildMember(MemberRequestDto memberRequestDto) {
        Member member = Member.builder()
                .email(memberRequestDto.email())
                .name(memberRequestDto.name())
                .userId(memberRequestDto.user_id())
                .password(memberRequestDto.password())
                .build();
        return member;
    }

    // TODO : 검색 로그 데이터 레디스에 저장
    public void insertRedisSearchLog(ZSetOperations<String, String> ops, String searchName, Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        Long score = now.toEpochSecond(ZoneOffset.UTC);
        String key = String.format(SEARCH_LOG.name() + memberId);
        String value = searchName;
        ops.add(key, value, score);
    }

    // TODO : 검색어 검색 시 인기 검색어 점수 + 1, memberReadService에 getHighScoreSearchWord에 응용됨
    public void insertRedisSearchRanking(ZSetOperations<String, String> ops, String searchName) {
        String key = String.format(SEARCH_RANK.name());
        String value = searchName;
        ops.incrementScore(key, value, 1);
    }


}
