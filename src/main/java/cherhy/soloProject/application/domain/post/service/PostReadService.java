package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    public List<PostPhotoDto> findPostByMemberId(Long memberId){
        List<Post> findPosts = postRepository.findAllByMemberId(memberId);
        List<PostPhotoDto> collect = changePostPhotoDto(findPosts);
        return collect;
    }

    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Pageable pageable) {
        Page<PostPhotoDto> findPosts = postRepository.findAllByMemberId(memberId, pageable);
        return findPosts;
    }

    public PageScroll<PostPhotoDto> findPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {

        List<Post> findPosts = postRepository.findByMemberIdPostIdDesc(memberId, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = changePostPhotoDto(findPosts);
        long nextKey = getNextKey(postPhotoDtos);
        return new PageScroll<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    private long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    //Dto 변환
    private List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
