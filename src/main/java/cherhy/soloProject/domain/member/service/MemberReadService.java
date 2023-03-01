package cherhy.soloProject.domain.member.service;

import cherhy.soloProject.domain.member.dto.request.SignInRequestDto;
import cherhy.soloProject.domain.member.dto.response.MemberSearchResponseDto;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.ExistException;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PasswordNotMatchException;
import cherhy.soloProject.application.exception.SameMemberException;
import cherhy.soloProject.application.exception.enums.ExceptionKey;
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

    public List<MemberSearchResponseDto> searchMember(String searchName, Long memberId) {
        List<Member> findMemberList = getMemberList(searchName);
        List<MemberSearchResponseDto> findMembers = changeMemberSearchResponseDto(findMemberList);
        insertRedisSearchLog(searchName, memberId);
        return findMembers;
    }

    private void insertRedisSearchLog(String searchName, Long memberId) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();

        LocalDateTime now = LocalDateTime.now();
        Long score = now.toEpochSecond(ZoneOffset.UTC);
        String key = String.format(SEARCH_LOG + memberId);
        String value = searchName;
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
            throw new ExistException(ExceptionKey.EMAIL);
        });
    }

    private void duplicateCheckUserId(String userId) {
        memberRepository.findByUserId(userId).ifPresent(m -> {
            throw new ExistException(ExceptionKey.ID);
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
        String formatSearchMemberName = String.format("%%%s%%", searchMemberName);
        return memberRepository.findTop3ByNameLikeOrderByIdAsc(formatSearchMemberName).orElseThrow(MemberNotFoundException::new);
    }

    private List<MemberSearchResponseDto> changeMemberSearchResponseDto(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchResponseDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

    public void SameUserCheck(Long memberId, Long blockMemberId) {
         if (memberId == blockMemberId) throw new SameMemberException();
    }
}
