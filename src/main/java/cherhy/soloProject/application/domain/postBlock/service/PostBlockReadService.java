package cherhy.soloProject.application.domain.postBlock.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.jpa.PostBlockRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PostBlockNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostBlockReadService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostBlockRepository postBlockRepository;
    public List<PostBlockResponseDto> getBlockPost(Long memberId) { // 페이징 해야함
        Member member = getMember(memberId);
        List<PostBlock> postBlocks = getPostBlocks(member);
        List<PostBlockResponseDto> res = changePostBlockResponseDto(postBlocks);
        return res;
    }

    private List<PostBlockResponseDto> changePostBlockResponseDto(List<PostBlock> postBlocks) {
        List<PostPhotoDto> postPhotoDtos = getPostPhotoDtos(postBlocks);
        return postPhotoDtos.stream().map(p -> new PostBlockResponseDto(p.getId(), p)).collect(Collectors.toList());
    }

    private List<PostPhotoDto> getPostPhotoDtos(List<PostBlock> postBlocks) {
        List<PostPhotoDto> collect = postBlocks.stream().map(p -> new PostPhotoDto(p.getPost())).collect(Collectors.toList());
        return collect;
    }

    private List<PostBlock> getPostBlocks(Member member) {
        return postBlockRepository.findByMember(member.getId()).orElseThrow(PostBlockNotFoundException::new);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }

}
