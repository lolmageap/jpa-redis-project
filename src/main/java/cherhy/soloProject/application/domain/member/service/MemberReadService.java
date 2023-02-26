package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.request.MemberSearchRequestDto;
import cherhy.soloProject.application.domain.member.dto.request.SignInRequestDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.ExistException;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PasswordNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static cherhy.soloProject.application.key.ExceptionKey.EMAIL;
import static cherhy.soloProject.application.key.ExceptionKey.ID;
import static cherhy.soloProject.application.key.RedisKey.SEARCH_LOG;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final StringRedisTemplate redisTemplate;


    public ResponseEntity emailCheck(String email){
        duplicateCheckEmail(email);
        return ResponseEntity.ok(200);
    }

    public ResponseEntity idCheck(String userId){
        duplicateCheckUserId(userId);
        return ResponseEntity.ok(200);
    }

    public ResponseEntity signIn(SignInRequestDto signInRequestDto, HttpSession session){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Member findMember = getMember(signInRequestDto);

        passwordComparison(signInRequestDto, findMember);
        session.setAttribute("userData" , findMember);
        attendToday(ops, findMember);
        return ResponseEntity.ok(200);
    }

    public List<MemberSearchRequestDto> searchMember(MemberSearchRequestDto memberSearchRequestDto) {
        List<Member> findMemberList = getMemberList(memberSearchRequestDto.searchName());
        List<MemberSearchRequestDto> findMembers = changeMemberSearchResponseDto(findMemberList);
        insertRedisSearchLog(memberSearchRequestDto);
        return findMembers;
    }

    private void insertRedisSearchLog(MemberSearchRequestDto memberSearchRequestDto) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();

        LocalDateTime now = LocalDateTime.now();
        Long score = now.toEpochSecond(ZoneOffset.UTC);
        String key = String.format(SEARCH_LOG + memberSearchRequestDto.memberId());
        String value = memberSearchRequestDto.searchName();
        ops.add(key, value, score);
    }

    private String formatToday() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

    public Member getMember(SignInRequestDto signInRequestDto) {
        return memberRepository.findByUserId(signInRequestDto.userId()).orElseThrow(MemberNotFoundException::new);
    }

    private void duplicateCheckEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new ExistException(EMAIL);
        });
    }

    private void duplicateCheckUserId(String userId) {
        memberRepository.findByUserId(userId).ifPresent(m -> {
            throw new ExistException(ID);
        });
    }

    private void attendToday(ValueOperations<String, String> ops, Member findMember) {
        String format = formatToday();
        ops.setBit(format, findMember.getId(), true);
    }

    private void passwordComparison(SignInRequestDto signInRequestDto, Member findMember) {
        if(!encoder.matches(signInRequestDto.password(), findMember.getPassword())){
            throw new PasswordNotMatchException();
        }
    }

    private List<Member> getMemberList(String searchMemberName) {
        return memberRepository.findByName(searchMemberName).orElseThrow(MemberNotFoundException::new);
    }

    private List<MemberSearchRequestDto> changeMemberSearchResponseDto(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchRequestDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }
}
