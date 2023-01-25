package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.Util.ScrollRequest;
import cherhy.soloProject.Util.PageScroll;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<PostPhotoDto> findPosts = new ArrayList<>();
        long nextKey = 0;

        if (scrollRequest.hasKey()){
            findPosts = postRepository.findByMemberIdPostIdDesc(memberId, scrollRequest);
        }else {
            findPosts = postRepository.findAllByMemberIdNoKey(memberId, scrollRequest);
        }

        nextKey = getNextKey(findPosts);
        return new PageScroll<>(scrollRequest.next(nextKey) ,findPosts);
    }

    private long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    //Dto 변환
    private List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
