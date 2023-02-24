package cherhy.soloProject.application.domain.postBlock.service;

import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.postBlock.dto.response.PostBlockResponseDto;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import cherhy.soloProject.application.domain.postBlock.repository.jpa.PostBlockRepository;
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
    private final PostBlockRepository postBlockRepository;

    public List<PostBlock> getPostBlocks(Member member) {
        return postBlockRepository.findByMember(member.getId()).orElseThrow(PostBlockNotFoundException::new);
    }

    public List<PostBlockResponseDto> changePostBlockResponseDto(List<PostPhotoDto> postPhotoDtos) {
        return postPhotoDtos.stream().map(p -> new PostBlockResponseDto(p.getId(), p)).collect(Collectors.toList());
    }

    public List<PostPhotoDto> getPostPhotoDtos(List<PostBlock> postBlocks) {
        return postBlocks.stream().map(p -> new PostPhotoDto(p.getPost())).collect(Collectors.toList());
    }

}
