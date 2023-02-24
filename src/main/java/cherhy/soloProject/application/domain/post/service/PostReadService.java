package cherhy.soloProject.application.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.repository.jpa.MemberRepository;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.domain.TimeLine.repository.jpa.TimeLineRepository;
import cherhy.soloProject.application.exception.MemberNotFoundException;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    public List<Post> getPostByMemberId(Member member) {
        return postRepository.findAllByMemberId(member.getId());
    }
    public List<Post> getPostByMemberId(Member member, Member myMember) {
        return postRepository.findPostByMemberId(member.getId(), myMember.getId());
    }

    public Post getPost(Long post) {
        return postRepository.findById(post)
                .orElseThrow(PostNotFoundException::new);
    }

    public Long getPostCountPage(Long memberId) {
        return postRepository.findAllByMemberIdCount(memberId);
    }

    public Long getPostCountPage(Long memberId, Long memberSessionId) {
        return postRepository.findAllByMemberIdCount(memberId, memberSessionId);
    }

    public List<Post> getPostByMemberIdPage(Long memberId, Pageable pageable) {
        return postRepository.findAllByMemberId(memberId, pageable);
    }

    public List<Post> getPostByMemberIdPage(Long memberId, Long memberSessionId, Pageable pageable) {
        return postRepository.findAllByMemberId(memberId, memberSessionId, pageable);
    }

    public List<Post> getPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(memberId, scrollRequest);
    }

    public List<Post> getPostByMemberIdCursor(Long memberId, Long memberSessionId, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(memberId, memberSessionId, scrollRequest);
    }


    public long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

    public List<PostPhotoDto> changePostPhotoDto(List<Post> findPosts) {
        return findPosts.stream().map(post ->
                new PostPhotoDto(
                post.getId(), post.getMember().getId(), post.getTitle(), post.getContent(), post.getLikeCount(), post.getPhotos())
        ).collect(Collectors.toList());
    }

}
