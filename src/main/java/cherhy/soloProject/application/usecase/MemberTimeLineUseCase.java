package cherhy.soloProject.application.usecase;

import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.Util.scrollDto.ScrollResponse;
import cherhy.soloProject.domain.TimeLine.service.TimeLineReadService;
import cherhy.soloProject.domain.TimeLine.service.TimeLineWriteService;
import cherhy.soloProject.domain.follow.service.FollowReadService;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.member.service.MemberReadService;
import cherhy.soloProject.domain.memberBlock.service.MemberBlockReadService;
import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
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


@Service
@Transactional
@RequiredArgsConstructor
public class MemberTimeLineUseCase {

    private final MemberReadService memberReadService;
    private final PostReadService postReadService;
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimeLineWriteService timeLineWriteService;
    private final TimeLineReadService timeLineReadService;
    private final MemberBlockReadService memberBlockReadService;

    public ResponseEntity createPost(PostRequestDto postRequestDto, Member member){
        Post post = Post.of(postRequestDto, member);
        Post savePost = postWriteService.save(post);

        List<Member> findMembers = followReadService.findAllByFollowers(member);
        ResponseEntity result = timeLineWriteService.uploadTimeLine(findMembers, savePost);
        return result;
    }

    public ResponseEntity modifyPost(PostRequestDto postRequestDto, Member member, Long postId){
        Post post = postReadService.getMyPost(postId, member);
        Post modifyPost = postWriteService.update(postRequestDto, post);
        postWriteService.save(modifyPost);
        return ResponseEntity.ok("수정 성공");
    }
    @Cacheable(cacheNames = "postAll1", key = "#memberId", cacheManager = "cacheManager")
    public List<PostPhotoDto> findPostByMemberId(Long memberId){
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMember(member);
        List<PostPhotoDto> result = PostPhotoDto.from(findPosts);
        return result;
    }

    @Cacheable(cacheNames = "postAll2", key = "#memberId", cacheManager = "cacheManager")
    public List<PostPhotoDto> findPostByMemberId(Long memberId, Member myMember){
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMember(member, myMember);
        List<PostPhotoDto> result = PostPhotoDto.from(findPosts);
        return result;
    }
    @Cacheable(cacheNames = "postPage1", key = "#memberId + #pageable.pageNumber", cacheManager = "cacheManager")
    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMemberIdPage(member, pageable);
        List<PostPhotoDto> postPhotoDtos = PostPhotoDto.from(findPosts);
        Long count = postReadService.getPostCountPage(memberId);
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }
    @Cacheable(cacheNames = "postPage2", key = "#memberId + #pageable.pageNumber", cacheManager = "cacheManager")
    public Page<PostPhotoDto> findPostByMemberIdPage(Long memberId, Member myMember, Pageable pageable) {
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMemberIdPage(member, myMember, pageable);
        List<PostPhotoDto> postPhotoDtos = PostPhotoDto.from(findPosts);
        Long count = postReadService.getPostCountPage(memberId, myMember.getId());
        return new PageImpl<>(postPhotoDtos,pageable,count);
    }

    @Cacheable(cacheNames = "postCursor1", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )"
            , cacheManager = "cacheManager")
    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(member, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = PostPhotoDto.from(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    @Cacheable(cacheNames = "postCursor2", key = "#memberId.toString() + '_' + #memberSessionId.toString()" +
            " + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )", cacheManager = "cacheManager")
    public ScrollResponse<PostPhotoDto> findPostByMemberIdCursor(Long memberId, Member myMember, ScrollRequest scrollRequest) {
        Member member = memberReadService.getMember(memberId);
        memberBlockReadService.ifIBlock(myMember,member);
        List<Post> findPosts = postReadService.getPostByMemberIdCursor(member, myMember, scrollRequest);
        List<PostPhotoDto> postPhotoDtos = PostPhotoDto.from(findPosts);
        long nextKey = postReadService.getNextKey(postPhotoDtos);
        return new ScrollResponse<>(scrollRequest.next(nextKey) ,postPhotoDtos);
    }

    @Cacheable(cacheNames = "timeLineCache", key = "#memberId.toString() + '_' + ( #scrollRequest.key() != null ? #scrollRequest.key() : '' )")
    public ScrollResponse<PostPhotoDto> getTimeLine(Member member, ScrollRequest scrollRequest) {
        List<Post> findPostIdByCoveringIndex = timeLineReadService.getTimeLine(member, scrollRequest);
        List<Long> key = timeLineReadService.getTimeLineNextKey(scrollRequest, member);
        Long nextKey = timeLineReadService.getNextKey(scrollRequest, key);
        List<PostPhotoDto> postPhotoDtos = PostPhotoDto.from(findPostIdByCoveringIndex);
        return new ScrollResponse<>(scrollRequest.next(nextKey),postPhotoDtos);
    }

}
