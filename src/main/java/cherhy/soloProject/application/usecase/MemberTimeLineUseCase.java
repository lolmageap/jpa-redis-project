package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.TimeLine.service.TimeLineReadService;
import cherhy.soloProject.application.domain.TimeLine.service.TimeLineWriteService;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.member.service.MemberReadService;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public String createPost(PostRequestDto postRequestDto){
        Member findMember = memberReadService.getMember(postRequestDto.memberId());
        Post addPost = postWriteService.buildPost(postRequestDto, findMember);
        String result = timeLineWriteService.insertTimeLineValue(findMember, addPost);
        postWriteService.addPostLikeToRedis(addPost);
        return result;
    }

    public ResponseEntity modifyPost(PostRequestDto postRequestDto, Long postId){
        Post findPost = postReadService.getPost(postId);
        postWriteService.modify(postRequestDto, findPost);
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
        List<Post> findPosts = postReadService.getPostByMemberId(member,myMember);
        List<PostPhotoDto> result = postReadService.changePostPhotoDto(findPosts);
        return result;
    }

    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Pageable pageable) {
        List<Post> findPosts = postReadService.getPostByMemberIdPage(memberId, pageable);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        Long count = postReadService.getPostCountPage(memberId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }

    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Long memberSessionId , Pageable pageable) {
        List<Post> findPosts = postReadService.getPostByMemberIdPage(memberId, memberSessionId, pageable);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        Long count = postReadService.getPostCountPage(memberId, memberSessionId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }

    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(memberId, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, Long memberSessionId, ScrollRequest scrollRequest) {
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(memberId, memberSessionId, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    public ScrollResponse<PostPhotoDto> getTimeLine(Long member_id, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(member_id);
        List<Post> findPostIdByCoveringIndex = timeLineReadService.getPostIdByMemberFromTimeLineCursor(member, scrollRequest);
        List<LocalDateTime> key = timeLineReadService.getTimeLineNextKey(scrollRequest, member);
        Long nextKey = timeLineReadService.getNextKey(scrollRequest, key);
        List<PostPhotoDto> postPhotoDtos = postReadService.changePostPhotoDto(findPostIdByCoveringIndex);
        return new ScrollResponse<>(scrollRequest.next(nextKey),postPhotoDtos);
    }

}
