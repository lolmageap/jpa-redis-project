package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.repository.jpa.PostBlockRepository;
import cherhy.soloProject.exception.PostBlockNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostBlockReadService {
    private final PostBlockRepository postBlockRepository;

    // TODO : 게시글 차단 목록 조회
    public List<PostBlock> getPostBlocks(Member member) {
        return postBlockRepository.findByMemberIdOrderByPostAsc(member.getId()).orElseThrow(PostBlockNotFoundException::new);
    }

    // TODO : 무한스크롤 게시글 차단 목록 조회
    public List<PostBlock> getPostBlocks(Member member, ScrollRequest scrollRequest) {
        return postBlockRepository.findByPostBlockScroll(member, scrollRequest).orElseThrow(PostBlockNotFoundException::new);
    }

    // TODO : 회원 Id와 게시글 Id로 차단 조회
    public Optional<PostBlock> getPostBlockByMemberIdAndPostId(Long memberId, Long postId) {
        return postBlockRepository.findByMemberIdAndPostId(memberId, postId);
    }

    // TODO : 무한스크롤 다음 키 조회
    public long getNextKey(List<PostBlockResponseDto> postBlockResponseDtos) {
        return postBlockResponseDtos.stream().mapToLong(v -> v.post().getId())
                .max().orElse(ScrollRequest.NONE_KEY);
    }

}
