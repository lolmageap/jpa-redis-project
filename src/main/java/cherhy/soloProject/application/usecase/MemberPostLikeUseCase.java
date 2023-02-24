package cherhy.soloProject.application.usecase;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.domain.postLike.entity.PostLike;
import cherhy.soloProject.application.domain.postLike.service.PostLikeReadService;
import cherhy.soloProject.application.domain.postLike.service.PostLikeWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static cherhy.soloProject.application.RedisKey.POST_LIKE;


@Service
@RequiredArgsConstructor
public class MemberPostLikeUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final PostLikeWriteService postLikeWriteService;
    private final PostLikeReadService postLikeReadService;
    private final StringRedisTemplate redisTemplate;


    public String postExample(PostLikeDto postLikeDto){
        Post findPost = postReadService.getPost(postLikeDto.PostId());
        findPost.updatePostLikeCount(findPost.getLikeCount()+1);
        Post save = postWriteService.save(findPost);
        Integer likeCount = save.getLikeCount();
        return Long.toString(likeCount);
    }

    public String postLike(PostLikeDto postLikeDto){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Member findMember = memberReadService.getMember(postLikeDto.memberId());
        Post findPost = postReadService.getPost(postLikeDto.PostId());
        String formatPost = String.format(POST_LIKE + findPost.getId()); //format 문법 변경
        Optional<PostLike> postLike = postLikeReadService.getMemberIdAndPostId(findMember, findPost);
        String result = postLikeWriteService.likeOrLikeCancel(ops, findMember, findPost, formatPost, postLike);

//        //레디스에서 게시물 확인 및 연관관계 확인 : 게시물 좋아요 누른 유저 정보 넣기 (Bulk insert를 위해)
//        String findPostLikeFromRedis = ops.get(formatPost);
//        String path = String.valueOf(post.getId());
        return result;
    }

}
