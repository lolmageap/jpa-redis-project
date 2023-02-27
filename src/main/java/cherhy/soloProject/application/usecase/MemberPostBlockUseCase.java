package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockReadService;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockWriteService;
import lombok.RequiredArgsConstructor;
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

    public ResponseEntity blockPost(Long memberId, Long postId) {
        Member member = memberReadService.getMember(memberId);
        Post post = postReadService.getPost(postId);
        Optional<PostBlock> postBlock = postBlockWriteService.getPostBlockByMemberIdAndPostId(memberId, postId);
        postBlock.ifPresentOrElse(p -> postBlockWriteService.unblock(p),
                () -> postBlockWriteService.block(member, post));
        return ResponseEntity.ok(200);
    }

    public List<PostBlockResponseDto> getBlockPost(Long memberId) {
        Member member = memberReadService.getMember(memberId);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        return  postBlockReadService.changePostBlockResponseDto(postBlocks);
    }

    public ScrollResponse getBlockPost(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member, scrollRequest);
        List<PostBlockResponseDto> postBlockResponseDtos = postBlockReadService.changePostBlockResponseDto(postBlocks);
        long nextKey = postBlockReadService.getNextKey(postBlockResponseDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey), postBlockResponseDtos);
    }

}
