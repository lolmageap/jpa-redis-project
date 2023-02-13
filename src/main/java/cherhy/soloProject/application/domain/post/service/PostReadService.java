package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.post.repository.jpa.TimeLineRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final TimeLineRepository timeLineRepository;

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

    public PageScroll<PostPhotoDto> getTimeLine(Long member_id, ScrollRequest scrollRequest) {
        Member member = getMember(member_id);
        List<Post> findPostIdByCoveringIndex = timeLineRepository.findPostIdByMemberFromTimeLine(member, scrollRequest);
        List<LocalDateTime> key = timeLineRepository.getNextKey(member, scrollRequest);
        Long nextKey = getNextKey(scrollRequest, key);
        List<PostPhotoDto> postPhotoDtos = changePostPhotoDto(findPostIdByCoveringIndex);
        return new PageScroll<>(scrollRequest.next(nextKey),postPhotoDtos);
    }

    private long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    private long getNextKey(ScrollRequest scrollRequest, List<LocalDateTime> covering) {
        return covering.stream().mapToLong(v -> Long.parseLong(v.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSS"))))
                .min().orElse(scrollRequest.NONE_KEY);
    }

    private Member getMember(Long member_id) {
        return memberRepository.findById(member_id).orElseThrow(MemberNotFoundException::new);
    }

    private List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
