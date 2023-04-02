package cherhy.soloProject.domain.post.service;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.repository.jpa.PostRepository;
import cherhy.soloProject.application.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostReadService {

    private final PostRepository postRepository;

    // TODO : MemberId로 게시글 조회
    public List<Post> getPostByMember(Member member) {
        return postRepository.findAllByMemberId(member.getId());
    }

    // TODO : 내 Id와 MemberId로 게시글 조회
    public List<Post> getPostByMember(Member member, Member myMember) {
        return postRepository.findPostByMemberId(member.getId(), myMember.getId());
    }

    // TODO : 게시글 조회
    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    // TODO : 게시글 아이디와 memberId로 게시글 조회
    public Post getPost(Long postId, Member member) {
        return postRepository.findByIdAndMemberId(postId, member.getId())
                .orElseThrow(PostNotFoundException::new);
    }

    // TODO : 페이지네이션 total 조회
    public Long getPostCountPage(Long memberId) {
        return postRepository.findAllByMemberIdCount(memberId);
    }

    // TODO : 나의 id를 포함하여 포함하여 페이지네이션 total 조회
    public Long getPostCountPage(Long memberId, Long memberSessionId) {
        return postRepository.findAllByMemberIdCount(memberId, memberSessionId);
    }

    // TODO : 페이지네이션 게시물 조회
    public List<Post> getPostByMemberIdPage(Member member, Pageable pageable) {
        return postRepository.findAllByMemberId(member.getId(), pageable);
    }

    // TODO : 나의 id를 포함하여 페이지네이션 게시물 조회
    public List<Post> getPostByMemberIdPage(Member member, Member myMember, Pageable pageable) {
        return postRepository.findAllByMemberId(member.getId(), myMember.getId(), pageable);
    }

    // TODO : 무한스크롤 게시글 조회
    public List<Post> getPostByMemberIdCursor(Member member, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(member.getId(), scrollRequest);
    }

    // TODO : 나의 id를 포함하여 무한스크롤 게시글 조회
    public List<Post> getPostByMemberIdCursor(Member member, Member myMember, ScrollRequest scrollRequest) {
        return postRepository.findByMemberIdPostIdDesc(member.getId(), myMember.getId(), scrollRequest);
    }

    // TODO : 무한스크롤 다음 키 가져오기
    public long getNextKey(List<PostPhotoDto> findPosts) {
        return findPosts.stream().mapToLong(v -> v.getId())
                .min().orElse(ScrollRequest.NONE_KEY);
    }

}
