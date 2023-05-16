package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponse;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.service.PostBlockReadService;
import cherhy.soloProject.domain.postBlock.service.PostBlockWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockUseCase {

    private final PostReadService postReadService;
    private final PostBlockReadService postBlockReadService;
    private final PostBlockWriteService postBlockWriteService;

    public ResponseEntity blockPost(Member member, Long postId) {
        Post post = postReadService.getPost(postId);
        Optional<PostBlock> postBlock = postBlockReadService.getPostBlockByMemberIdAndPostId(member.getId(), postId);

        postBlock.ifPresentOrElse(p -> postBlockWriteService.unblock(p),
                () -> postBlockWriteService.block(PostBlock.of(member, post)));
        return ResponseEntity.ok("성공");
    }
    @Cacheable(cacheNames = "blockPost", key = "#memberId", cacheManager = "cacheManager")
    public List<PostBlockResponse> getBlockPost(Member member) {
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        return postBlocks.stream().map(PostBlockResponse::of).collect(Collectors.toList());
    }
    @Cacheable(cacheNames = "blockPostCursor", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse getBlockPost(Member member, ScrollRequest scrollRequest) {
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member, scrollRequest);
        List<PostBlockResponse> postBlock =
                postBlocks.stream().map(PostBlockResponse::of).collect(Collectors.toList());
        long nextKey = postBlockReadService.getNextKey(postBlock);
        return new ScrollResponse<>(scrollRequest.next(nextKey), postBlock);
    }

}
