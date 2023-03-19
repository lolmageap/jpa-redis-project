package cherhy.soloProject.application.scheduler;

import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static cherhy.soloProject.application.key.RedisKey.POST_LIKE;

@Service
@RequiredArgsConstructor
public class PostLikeScheduler {

    private final StringRedisTemplate redisTemplate;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;

    // TODO : fixedDelay마다 레디스에 있는 좋아요 처리된 데이터 10개씩 RDBMS에 Update
    @Async
    @Scheduled(fixedDelay = 5000)
    public void scheduler(){
        ScanOptions scanOptions = ScanOptions.scanOptions().match("postLike*").count(10).build();
        Cursor<String> cursor = redisTemplate.scan(scanOptions);

        while (cursor.hasNext()){
            String key = cursor.next().toString();
            Long postId = extractPostId(key);
            if (postId == null) continue;

            String value = redisTemplate.opsForValue().get(key);
            Post post = postReadService.getPost(postId);
            post.updatePostLikeCount(Integer.parseInt(value));
            Post savePost = postWriteService.save(post);
            deleteRedisKey(savePost);
        }
    }

    // TODO : Redis에서 Post Id 추출하기
    private Long extractPostId(String key) {
        if (key.contains(POST_LIKE.name())){
            int index = key.indexOf(":");
            Long postId = Long.valueOf(key.substring(index + 1));
            return postId;
        }else {
            return null;
        }
    }

    // TODO : RDBMS에 좋아요 update 후 레디스 키 삭제
    private void deleteRedisKey(Post savePost) {
        String formatPost = String.format(POST_LIKE.name() + savePost.getId());
        redisTemplate.delete(formatPost);
    }

}
