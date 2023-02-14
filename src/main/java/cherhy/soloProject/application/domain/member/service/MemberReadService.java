package cherhy.soloProject.application.domain.member.service;

import cherhy.soloProject.application.domain.member.dto.SignInDto;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.exception.ExistException;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PasswordNotMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

        if(!encoder.matches(signInDto.password(), findMember.getPassword())){
            throw new PasswordNotMatchException();
        }

        session.setAttribute("userData" , findMember);

        attendToday(ops, findMember);
        return findMember.getName();
    }

    private void attendToday(ValueOperations<String, String> ops, Member findMember) {
        String format = formatDate();
        ops.setBit(format, findMember.getId(), true);
    }

    private String formatDate() {
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
    private void duplicateCheckUserId(String userId) {
        Optional<Member> member = memberRepository.findByUserId(userId);
        if (!member.isEmpty()){
            throw new ExistException("id");
        }
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
