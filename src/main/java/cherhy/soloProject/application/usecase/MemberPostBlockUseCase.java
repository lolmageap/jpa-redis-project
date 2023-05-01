package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
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


@Service
@Transactional
@RequiredArgsConstructor
public class MemberPostBlockUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostBlockReadService postBlockReadService;
    private final PostBlockWriteService postBlockWriteService;

    public ResponseEntity blockPost(Member member, Long postId) {
        Post post = postReadService.getPost(postId);
        Optional<PostBlock> postBlock = postBlockReadService.getPostBlockByMemberIdAndPostId(member.getId(), postId);

        postBlock.ifPresentOrElse(p -> postBlockWriteService.unblock(p),
                () -> postBlockWriteService.block(member, post));
        return ResponseEntity.ok("성공");
    }
    @Cacheable(cacheNames = "blockPost", key = "#memberId", cacheManager = "cacheManager")
    public List<PostBlockResponseDto> getBlockPost(Member member) {
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        return PostBlockResponseDto.from(postBlocks);
    }
    @Cacheable(cacheNames = "blockPostCursor", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse getBlockPost(Member member, ScrollRequest scrollRequest) {
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member, scrollRequest);
        List<PostBlockResponseDto> postBlockResponseDtos = PostBlockResponseDto.from(postBlocks);
        long nextKey = postBlockReadService.getNextKey(postBlockResponseDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey), postBlockResponseDtos);
    }

}
