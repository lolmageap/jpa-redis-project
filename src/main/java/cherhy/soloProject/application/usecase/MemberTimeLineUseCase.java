package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.domain.TimeLine.service.TimeLineReadService;
import cherhy.soloProject.domain.TimeLine.service.TimeLineWriteService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import cherhy.soloProject.application.exception.MemberBlockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberTimeLineUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final TimeLineWriteService timeLineWriteService;
    private final TimeLineReadService timeLineReadService;
    private final MemberBlockReadService memberBlockReadService;

    public ResponseEntity createPost(PostRequestDto postRequestDto, Long memberId){
        Member findMember = memberReadService.getMember(memberId);
        Post addPost = postWriteService.buildPost(postRequestDto, findMember);
        ResponseEntity result = timeLineWriteService.insertTimeLineValue(findMember, addPost);
        postWriteService.addPostLikeToRedis(addPost);
        return result;
    }

    public ResponseEntity modifyPost(PostRequestDto postRequestDto, Long memberId, Long postId){
        Member member = memberReadService.getMember(memberId);
        Post findPost = postReadService.getPost(postId, member);
        Post modifyPost = postWriteService.modify(postRequestDto, findPost);
        postWriteService.save(modifyPost);
        return ResponseEntity.ok(200);
    }

    public List<PostPhotoDto> findPostByMemberId(Long memberId){
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMemberId(member);
        List<PostPhotoDto> result = postReadService.changePostPhotoDto(findPosts);
        return result;
    }

    public List<PostPhotoDto> findPostByMemberId(Long memberId, Long memberSessionId){
        Member member = memberReadService.getMember(memberId);
        Member myMember = memberReadService.getMember(memberSessionId);
        ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMemberId(member,myMember);
        List<PostPhotoDto> result = postReadService.changePostPhotoDto(findPosts);
        return result;
    }

    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMemberIdPage(member, pageable);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        Long count = postReadService.getPostCountPage(memberId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }
    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Long memberSessionId , Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        Member myMember = memberReadService.getMember(memberSessionId);
        ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMemberIdPage(member, myMember, pageable);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        Long count = postReadService.getPostCountPage(memberId, memberSessionId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }

    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(member, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, Long memberSessionId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        Member myMember = memberReadService.getMember(memberSessionId);
        ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(member, myMember, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    public ScrollResponse<PostPhotoDto> getTimeLine(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<Post> findPostIdByCoveringIndex = timeLineReadService.getPostIdByMemberFromTimeLineCursor(member, scrollRequest);
        List<Long> key = timeLineReadService.getTimeLineNextKey(scrollRequest, member);
        Long nextKey = timeLineReadService.getNextKey(scrollRequest, key);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPostIdByCoveringIndex);
        return new ScrollResponse<>(scrollRequest.next(nextKey),postPhotoDtos);
    }

    private void ifIBlock(Member myMember, Member blockMember) {
        memberBlockReadService.ifIBlock(myMember, blockMember).ifPresent(m -> {
            throw new MemberBlockException();
        });
    }
}
