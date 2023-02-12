package cherhy.soloProject.application.domain.postLike.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import cherhy.soloProject.application.domain.postLike.repository.jpa.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeWriteService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;
    private final PostLikeRepository postLikeRepository;



    public String postLike(PostLikeDto postLikeDto){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Member findMember = findMember(postLikeDto);
        Post findPost = findPost(postLikeDto.PostId());
        String formatPost = String.format("postLike:" + findPost.getId());
        Optional<PostLike> postLike = postLikeRepository.findByMemberIdAndPostId(findMember.getId(), findPost.getId());
        String result = likeOrLikeCancel(ops, findMember, findPost, formatPost, postLike);

//        //레디스에서 게시물 확인 및 연관관계 확인 : 게시물 좋아요 누른 유저 정보 넣기 (Bulk insert를 위해)
//        String findPostLikeFromRedis = ops.get(formatPost);
//        String path = String.valueOf(post.getId());
        return result;
    }

    private String likeOrLikeCancel(ValueOperations<String, String> ops, Member findMember, Post findPost, String formatPost, Optional<PostLike> postLike) {
        if (postLike.isEmpty()){
            //좋아요 테이블에 값이 없으면 좋아요 +1
            ops.increment(formatPost);
            buildPostLike(findMember, findPost);
            return "좋아요";
        }else {
            //좋아요 테이블에 값이 없으면 좋아요 +1
            ops.decrement(formatPost);
            postLikeRepository.delete(postLike.get());
            return "좋아요 취소";
        }
    }

    private PostLike buildPostLike(Member findMember, Post findPost) {
        PostLike buildPostLike = PostLike.builder()
                .member(findMember)
                .post(findPost)
                .build();
        PostLike savePostLike = postLikeRepository.save(buildPostLike);
        return savePostLike;
    }

    private Post findPost(Long PostId) {
        return postRepository.findById(PostId)
                .orElseThrow(() -> new NullPointerException("게시물이 존재하지 않습니다."));
    }

    private Member findMember(PostLikeDto postLikeDto) {
        return memberRepository.findById(postLikeDto.memberId())
                .orElseThrow(() -> new NullPointerException("회원 정보가 없습니다."));
    }

    @Async
    @Scheduled(fixedDelay = 5000)
    public void scheduler(){
        ScanOptions scanOptions = ScanOptions.scanOptions().match("*").count(10).build();
        Cursor<String> cursor = redisTemplate.scan(scanOptions);

        while (cursor.hasNext()){

            String key = new String(cursor.next());
            Long postId = 0L;
            postId = extractPostId(key);
            if (postId == null) continue;

            String value = redisTemplate.opsForValue().get(key);
            Post post = findPost(postId);
            post.updatePostLikeCount(Long.parseLong(value));
            Post savePost = postRepository.save(post);
            deleteRedisKey(savePost);

        }
    }

    private Long extractPostId(String key) {
        Long postId;
        if (key.contains("postLike:")){
            int index = key.indexOf(":");
            postId = Long.valueOf(key.substring(index + 1));
        }else {
            System.out.println("엘스");

            return null;
        }
        return postId;
    }

    private void deleteRedisKey(Post savePost) {
        String formatPost = String.format("postLike:" + savePost.getId());
        redisTemplate.delete(formatPost);
    }

}
