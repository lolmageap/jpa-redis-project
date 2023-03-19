package cherhy.soloProject.domain.member.service;

import cherhy.soloProject.application.exception.ExistException;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PasswordNotMatchException;
import cherhy.soloProject.application.exception.SameMemberException;
import cherhy.soloProject.application.exception.enums.ExceptionKey;
import cherhy.soloProject.domain.member.dto.request.SignInRequestDto;
import cherhy.soloProject.domain.member.dto.response.MemberSearchResponseDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cherhy.soloProject.application.key.RedisKey.SEARCH_LOG;
import static cherhy.soloProject.application.key.RedisKey.SEARCH_RANK;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final StringRedisTemplate redisTemplate;

    // TODO : 이메일 체크
    public ResponseEntity emailCheck(String email){
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new ExistException(ExceptionKey.EMAIL);
        });
        return ResponseEntity.ok("이메일이 사용 가능합니다");
    }

    // TODO : 아이디 체크
    public ResponseEntity idCheck(String userId){
        memberRepository.findByUserId(userId).ifPresent(m -> {
            throw new ExistException(ExceptionKey.ID);
        });
        return ResponseEntity.ok("아이디가 사용 가능합니다");
    }

    // TODO : 로그인
    public ResponseEntity signIn(SignInRequestDto signInRequestDto, HttpSession session){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Member findMember = getMember(signInRequestDto.userId());

        passwordComparison(signInRequestDto, findMember);
        session.setAttribute("userData" , findMember);
        attendToday(ops, findMember);
        return ResponseEntity.ok("로그인 성공");
    }

    // TODO : 회원 조회
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    // TODO : userId로 회원 조회
    public Member getMember(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(MemberNotFoundException::new);
    }

    // TODO : 출석체크
    private void attendToday(ValueOperations<String, String> ops, Member findMember) {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));;
        ops.setBit(format, findMember.getId(), true);
    }

    // TODO : 비밀번호 체크
    private void passwordComparison(SignInRequestDto signInRequestDto, Member findMember) {
        if(!encoder.matches(signInRequestDto.password(), findMember.getPassword())){
            throw new PasswordNotMatchException();
        }
    }

    // TODO : 회원 이름 검색, 3명 조회
    public List<Member> getMemberList(String searchMemberName) {
        String formatSearchMemberName = String.format("%%%s%%", searchMemberName);
        return memberRepository.findTop3ByNameLikeOrderByIdAsc(formatSearchMemberName).orElseThrow(MemberNotFoundException::new);
    }

    // TODO : 회원 빌드
    public List<Member> findAllByMember(Member findMember, Post post) {
        return memberRepository.findAllByFollowers(findMember.getId()).orElseThrow(MemberNotFoundException::new);
    }

    // TODO : 검색 DTO로 변환
    public List<MemberSearchResponseDto> changeMemberSearchResponseDto(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchResponseDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

    // TODO : 자기 자신은 팔로우, 차단 못하게 체크!
    public void SameUserCheck(Long memberId, Long blockMemberId) {
         if (memberId == blockMemberId) throw new SameMemberException();
    }

    // TODO : 검색 내역 조회
    public Set<String> getSearchHistoryLog(ZSetOperations<String, String> ops, Long memberId) {
        String key = String.format(SEARCH_LOG.name() + memberId);
        return ops.reverseRange(key, 0, 4);
    }

    // TODO : 검색 시 auto-complete에 사용, 연관성이 가장 높은 데이터 ex)구글 검색에서 단어하나 타이핑 할 때 마다 관련된 데이터 조회
    public Set<String> getHighScoreSearchWord(ZSetOperations<String, String> ops, String searchName) {

        char lastChar = searchName.charAt(searchName.length() - 1);
        char nextChar = (char) (lastChar + 1);

        return ops.reverseRangeByLex(SEARCH_RANK.name(), RedisZSetCommands.Range.range()
                .gte(searchName).lt(searchName.substring(0, searchName.length() - 1) + nextChar), RedisZSetCommands.Limit.limit().count(5));
    }

}
