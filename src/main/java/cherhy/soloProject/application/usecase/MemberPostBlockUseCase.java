package cherhy.soloProject.application.usecase;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import cherhy.soloProject.application.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockReadService;
import cherhy.soloProject.application.domain.postBlock.service.PostBlockWriteService;
import cherhy.soloProject.application.domain.postLike.service.PostLikeReadService;
import cherhy.soloProject.application.domain.postLike.service.PostLikeWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberPostBlockUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostBlockReadService postBlockReadService;
    private final PostBlockWriteService postBlockWriteService;

    public String blockPost(Long memberId, Long postId) {
        Member member = memberReadService.getMember(memberId);
        Post post = postReadService.getPost(postId);
        Optional<PostBlock> postBlock = postBlockWriteService.getPostBlockByMemberIdAndPostId(memberId, postId);
        return postBlockWriteService.blockOrUnblock(member, post, postBlock);
    }

    public List<PostBlockResponseDto> getBlockPost(Long memberId) { // 페이징 해야함
        Member member = memberReadService.getMember(memberId);
        List<PostBlock> postBlocks = postBlockReadService.getPostBlocks(member);
        List<PostPhotoDto> postPhotoDtos = postBlockReadService.getPostPhotoDtos(postBlocks);
        List<PostBlockResponseDto> res = postBlockReadService.changePostBlockResponseDto(postPhotoDtos);
        return res;
    }

}
