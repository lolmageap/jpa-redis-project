package cherhy.soloProject.application.domain.postLike.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    public String postLike(PostLikeDto postLikeDto){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Member findMember = findMember(postLikeDto);
        Post post = findPost(postLikeDto);
        String formatPost = String.format("postLike" + post.getId());
        String findPostLikeFromRedis = ops.get(formatPost);

        System.out.println("findPostLikeFromRedis = " + findPostLikeFromRedis);
        String path = String.valueOf(post.getId());
        Long increment = ops.increment(path);
        System.out.println("increment = " + increment);

        // 게시물 조회 로직
        // 좋아요 테이블 조회
        // 좋아요를 해놓은 데이터가 없으면 좋아요 테이블에 insert
        // 레디스에 조회 테이블 id 값과 조회수 increment
        // 좋아요를 해놨었다면 데이터를 delete
        // 레디스에 조회 테이블 id 값과 조회수 decrement
        return "성공";
    }

    private Post findPost(PostLikeDto postLikeDto) {
        return postRepository.findById(postLikeDto.PostId())
                .orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다."));
    }

    private Member findMember(PostLikeDto postLikeDto) {
        return memberRepository.findById(postLikeDto.memberId())
                .orElseThrow(() -> new NullPointerException("회원 정보가 없습니다."));
    }

//    @Async
//    @Scheduled(fixedDelay = 2000)
//    public void postLikeUpdate(){
//        // 스케쥴러를 사용하여 좋아요수 한번에 가져오기
//        // redis에서 계산된 값들을 전부 가져오고 bulk update
//        System.out.println("time = " + LocalDateTime.now());
//    }

}
