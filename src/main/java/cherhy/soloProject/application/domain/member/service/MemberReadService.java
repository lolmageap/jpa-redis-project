package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.MemberSearchDto;
import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.ExistException;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PasswordNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReadService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final StringRedisTemplate redisTemplate;


    public String emailCheck(String email){
        duplicateCheckEmail(email);
        return "사용 가능한 이메일입니다.";
    }
    public String idCheck(String userId){
        duplicateCheckUserId(userId);
        return "사용 가능한 아이디입니다.";
    }
    public String signIn(SignInDto signInDto, HttpSession session){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        Member findMember = getMember(signInDto);

        passwordComparison(signInDto, findMember);
        session.setAttribute("userData" , findMember);
        attendToday(ops, findMember);
        return findMember.getName();
    }

    public List<MemberSearchDto> searchMember(MemberSearchDto memberSearchDto) {
        List<Member> findMemberList = getMemberList(memberSearchDto.searchName());
        List<MemberSearchDto> findMembers = changeMemberSearchResponseDto(findMemberList);


        insertRedisSearchLog(memberSearchDto);

        return findMembers;
    }

    private void insertRedisSearchLog(MemberSearchDto memberSearchDto) {
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();

        LocalDateTime now = LocalDateTime.now();
        Long score = now.toEpochSecond(ZoneOffset.UTC);
        String key = String.format("SearchLog:%s", memberSearchDto.memberId());
        String value = memberSearchDto.searchName();
        ops.add(key, value, score);
    }

    private String formatToday() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private Member getMember(SignInDto signInDto) {
        return memberRepository.findByUserId(signInDto.userId()).orElseThrow(MemberNotFoundException::new);
    }

    private void duplicateCheckEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (!member.isEmpty()){
            throw new ExistException("email");
        }
    }
    private void attendToday(ValueOperations<String, String> ops, Member findMember) {
        String format = formatToday();
        ops.setBit(format, findMember.getId(), true);
    }
    private void duplicateCheckUserId(String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);
        if (!member.isEmpty()){
            throw new ExistException("id");
        }
    }

    private void passwordComparison(SignInDto signInDto, Member findMember) {
        if(!encoder.matches(signInDto.password(), findMember.getPassword())){
            throw new PasswordNotMatchException();
        }
    }
    private List<Member> getMemberList(String searchMemberName) {
        return memberRepository.findByName(searchMemberName).orElseThrow(MemberNotFoundException::new);
    }
    private List<MemberSearchDto> changeMemberSearchResponseDto(List<Member> findMemberList) {
        return findMemberList.stream()
                .map(m -> new MemberSearchDto(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

//    private boolean duplicateCheckEmail(String email) {
//        Optional<Member> member = memberRepository.findByEmail(email);
//        if (!member.isEmpty()){
//            return false;
//        }
//        return true;
//    }
//    private boolean duplicateCheckUserId(String userId) {
//        Optional<Member> member = memberRepository.findByUserId(userId);
//        if (!member.isEmpty()){
//            return false;
//        }
//        return true;
//    }
//
//    public ResponseEntity<String> getResponseEmail(Boolean aBoolean) {
//        if (aBoolean){
//            return ResponseEntity.ok("이메일이 사용 가능합니다");
//        }
//        return ResponseEntity.badRequest().body("이메일이 중복됩니다");
//    }
//    public ResponseEntity<String> getResponseId(Boolean aBoolean) {
//        if (aBoolean){
//            return ResponseEntity.ok("아이디가 사용 가능합니다");
//        }
//        return ResponseEntity.badRequest().body("아이디가 존재합니다");
//    }
}
