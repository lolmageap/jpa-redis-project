package cherhy.soloProject.domain.postBlock.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.domain.postBlock.repository.jpa.PostBlockRepository;
import cherhy.soloProject.application.exception.PostBlockNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostBlockReadService {
    private final PostBlockRepository postBlockRepository;

    public List<PostBlock> getPostBlocks(Member member) {
        return postBlockRepository.findByMemberIdOrderByPostAsc(member.getId()).orElseThrow(PostBlockNotFoundException::new);
    }

    public List<PostBlockResponseDto> changePostBlockResponseDto(List<PostBlock> postBlocks) {
        return postBlocks.stream().map(p -> new PostBlockResponseDto(p.getId(),new PostPhotoDto(p.getPost())))
                .collect(Collectors.toList());
    }

    // 여기 스크롤 페이징 처리해야함
    public List<PostBlock> getPostBlocks(Member member, ScrollRequest scrollRequest) {
        return postBlockRepository.findByPostBlockScroll(member, scrollRequest).orElseThrow(PostBlockNotFoundException::new);
    }

    public long getNextKey(List<PostBlockResponseDto> postBlockResponseDtos) {
        return postBlockResponseDtos.stream().mapToLong(v -> v.post().getId())
                .max().orElse(ScrollRequest.NONE_KEY);
    }

}
