package cherhy.soloProject.domain.member.service;

import cherhy.soloProject.domain.member.dto.request.MemberRequest;
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
    public ResponseEntity signUp(MemberRequest memberRequestDto) {
            Member member = BuildMember(memberRequestDto);
            member.changePassword(encoder.encode(memberRequestDto.password()));
            memberRepository.save(member);
        return ResponseEntity.ok("회원가입 완료");
    }

    // TODO : 회원 정보 수정
    public String modifyMember(MemberRequest memberRequestDto, Member member) {
        member.changeUserId(memberRequestDto.user_id());
        member.changeName(memberRequestDto.name());
        member.changePassword(encoder.encode(memberRequestDto.password()));
        return "회원정보 변경";
    }

    // TODO : 회원 빌드
    private Member BuildMember(MemberRequest memberRequestDto) {
        return Member.builder()
                .email(memberRequestDto.email())
                .name(memberRequestDto.name())
                .userId(memberRequestDto.user_id())
                .password(memberRequestDto.password())
                .build();
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
