package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
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
        postBlockWriteService.blockOrUnblock(member, post, postBlock);
        return ResponseEntity.ok(200);
    }

    public List<PostBlockResponseDto> getBlockPost(Long memberId) {
        Member member = memberReadService.getMember(memberId);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        List<PostPhotoDto> postPhotoDtos = postBlockReadService.getPostPhotoDtos(postBlocks);
        List<PostBlockResponseDto> res = postBlockReadService.changePostBlockResponseDto(postPhotoDtos);
        return res;
    }
    public List<PostBlockResponseDto> getBlockPost(ScrollRequest scrollRequest, Long memberId) { // 페이징
        Member member = memberReadService.getMember(memberId);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        List<PostPhotoDto> postPhotoDtos = postBlockReadService.getPostPhotoDtos(postBlocks);
        List<PostBlockResponseDto> res = postBlockReadService.changePostBlockResponseDto(postPhotoDtos);
        return res;
    }

}
