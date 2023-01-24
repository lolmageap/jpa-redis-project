package cherhy.soloProject.application.service.postService;

import cherhy.soloProject.Util.CursorRequest;
import cherhy.soloProject.Util.PageCursor;
import cherhy.soloProject.application.domain.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.entity.Post;
import cherhy.soloProject.application.repository.jpa.PostRepository;
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

    public PageCursor<PostPhotoDto> findPostByMemberIdCursor(Long memberId, CursorRequest cursorRequest) {
        List<PostPhotoDto> findPosts = new ArrayList<>();
        long nextKey = 0;

        if (cursorRequest.hasKey()){
            findPosts = postRepository.findByMemberIdPostIdDesc(memberId, cursorRequest);
        }else {
            findPosts = postRepository.findAllByMemberIdNoKey(memberId, cursorRequest);
        }

        nextKey = getNextKey(findPosts);
        return new PageCursor<>(cursorRequest.next(nextKey) ,findPosts);
    }

    private static long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(CursorRequest.NONE_KEY);
    }

    //Dto 변환
    private List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
