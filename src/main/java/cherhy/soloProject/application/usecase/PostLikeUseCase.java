package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.post.dto.response.PostPhotoResponse;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.postLike.dto.request.PostLikeRequest;
import cherhy.soloProject.domain.postLike.dto.response.PostLikeResponse;
import cherhy.soloProject.domain.postLike.entity.PostLike;
import cherhy.soloProject.domain.postLike.service.PostLikeReadService;
import cherhy.soloProject.domain.postLike.service.PostLikeWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cherhy.soloProject.application.key.RedisKey.POST_LIKE;


@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostLikeWriteService postLikeWriteService;
    private final PostLikeReadService postLikeReadService;
    private final StringRedisTemplate redisTemplate;


    public ResponseEntity postLike(PostLikeRequest postLikeDto){
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Member findMember = memberReadService.getMember(postLikeDto.memberId());
        Post findPost = postReadService.getPost(postLikeDto.PostId());
        String formatPost = String.format(POST_LIKE.name() + findPost.getId()); //format 문법 변경
        Optional<PostLike> postLike = postLikeReadService.getMemberIdAndPostId(findMember, findPost);
        postLikeWriteService.likeOrLikeCancel(ops, findMember, findPost, formatPost, postLike);

//        // 레디스에서 게시물 확인 및 연관관계 확인 : 게시물 좋아요 누른 유저 정보 넣기 (Bulk insert를 위해)
//        String findPostLikeFromRedis = ops.get(formatPost);
//        String path = String.valueOf(post.getId());

        return ResponseEntity.ok("성공");
    }

    public ScrollResponse getPostLike(Member member, ScrollRequest scrollRequest) {
        List<PostLikeResponse> findPosts = postLikeReadService.getPostLike(member, scrollRequest);
        long nextKey = postLikeReadService.getNextKey(findPosts);
        List<Post> posts = postLikeReadService.changePost(findPosts);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());

        return new ScrollResponse<>(scrollRequest.next(nextKey),postPhotoDtos);
    }
}
