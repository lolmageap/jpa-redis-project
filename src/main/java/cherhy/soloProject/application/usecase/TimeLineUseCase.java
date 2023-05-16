package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.domain.TimeLine.service.TimeLineReadService;
import cherhy.soloProject.domain.TimeLine.service.TimeLineWriteService;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.post.dto.response.PostPhotoResponse;
import cherhy.soloProject.domain.post.dto.request.PostRequest;
import cherhy.soloProject.domain.post.entity.Post;
import cherhy.soloProject.domain.post.service.PostReadService;
import cherhy.soloProject.domain.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TimeLineUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimeLineWriteService timeLineWriteService;
    private final TimeLineReadService timeLineReadService;
    private final MemberBlockReadService memberBlockReadService;

    public ResponseEntity createPost(PostRequest postRequestDto, Member member){
        Post post = Post.of(postRequestDto, member);
        Post savePost = postWriteService.save(post);

        List<Member> findMembers = followReadService.findAllByFollowers(member);
        ResponseEntity result = timeLineWriteService.uploadTimeLine(findMembers, savePost);
        return result;
    }

    public ResponseEntity modifyPost(PostRequest postRequestDto, Member member, Long postId){
        Post post = postReadService.getMyPost(postId, member);
        Post modifyPost = postWriteService.update(postRequestDto, post);
        postWriteService.save(modifyPost);
        return ResponseEntity.ok("수정 성공");
    }
    @Cacheable(cacheNames = "postAll1", key = "#memberId", cacheManager = "cacheManager")
    public List<PostPhotoResponse> findPostByMemberId(Long memberId){
        Member member = memberReadService.getMember(memberId);
        List<Post> posts = postReadService.getPostByMember(member);
        List<PostPhotoResponse> result = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        return result;
    }

    @Cacheable(cacheNames = "postAll2", key = "#memberId", cacheManager = "cacheManager")
    public List<PostPhotoResponse> findPostByMemberId(Long memberId, Member myMember){
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> posts = postReadService.getPostByMember(member, myMember);
        List<PostPhotoResponse> result = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        return result;
    }
    @Cacheable(cacheNames = "postPage1", key = "#memberId + #pageable.pageNumber", cacheManager = "cacheManager")
    public Page<PostPhotoResponse> findPostByMemberIdPage(Long memberId, Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        List<Post> posts = postReadService.getPostByMemberIdPage(member, pageable);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        Long count = postReadService.getPostCountPage(memberId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }
    @Cacheable(cacheNames = "postPage2", key = "#memberId + #pageable.pageNumber", cacheManager = "cacheManager")
    public Page<PostPhotoResponse> findPostByMemberIdPage(Long memberId, Member myMember, Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> posts = postReadService.getPostByMemberIdPage(member, myMember, pageable);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        Long count = postReadService.getPostCountPage(memberId, myMember.getId());
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }

    @Cacheable(cacheNames = "postCursor1", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse<PostPhotoResponse> findPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<Post> posts = postReadService.getPostByMemberIdCursor(member, scrollRequest);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    @Cacheable(cacheNames = "postCursor2", key = "#memberId.toString() + '_' + #memberSessionId.toString()" +
            " + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )", cacheManager = "cacheManager")
    public ScrollResponse<PostPhotoResponse> findPostByMemberIdCursor(Long memberId, Member myMember, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> posts = postReadService.getPostByMemberIdCursor(member, myMember, scrollRequest);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    @Cacheable(cacheNames = "timeLineCache", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )")
    public ScrollResponse<PostPhotoResponse> getTimeLine(Member member, ScrollRequest scrollRequest) {
        List<Post> posts = timeLineReadService.getTimeLine(member, scrollRequest);
        List<Long> key = timeLineReadService.getTimeLineNextKey(scrollRequest, member);
        Long nextKey = timeLineReadService.getNextKey(scrollRequest, key);
        List<PostPhotoResponse> postPhotoDtos = posts.stream().map(PostPhotoResponse::of).collect(Collectors.toList());
        return new ScrollResponse<>(scrollRequest.next(nextKey),postPhotoDtos);
    }

}
