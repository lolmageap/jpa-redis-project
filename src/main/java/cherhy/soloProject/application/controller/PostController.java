package cherhy.soloProject.application.controller;

import cherhy.soloProject.Util.scrollDto.PageScroll;
import cherhy.soloProject.Util.scrollDto.ScrollRequest;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.application.domain.post.service.PostReadService;
import cherhy.soloProject.application.domain.post.service.PostWriteService;
import cherhy.soloProject.application.domain.postLike.dto.PostLikeDto;
import cherhy.soloProject.application.domain.postLike.service.PostLikeWriteService;
import cherhy.soloProject.application.utilService.SessionReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "게시물")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final SessionReadService sessionReadService;
    private final PostLikeWriteService postLikeWriteService;

    @Operation(summary = "게시물 생성 // Push Model")
    @PostMapping("/create")
    public String createPost(@RequestBody @Valid PostRequestDto postRequestDto){
        return postWriteService.createPost(postRequestDto);
    }

    @Operation(summary = "게시물 수정")
    @PutMapping("/modify/{postId}")
    public String modifyPost(@RequestBody @Valid PostRequestDto postRequestDto, @PathVariable Long postId){
        return postWriteService.modifyPost(postRequestDto, postId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 전체 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{member_id}")
    public List<PostPhotoDto> getPost(@PathVariable("member_id") Long memberId, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);

        if (memberSessionId == null){
            return postReadService.findPostByMemberId(memberId);
        }

        return postReadService.findPostByMemberId(memberId,memberSessionId);
    }

    @Operation(summary = "사용자의 게시물 불러오기, 페이징 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{memberId}/page")
    public Page<PostPhotoDto> getPostPage(@PathVariable Long memberId, Pageable pageable, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);

        if (memberSessionId == null){
            return postReadService.findPostByMemberIdPage(memberId, pageable);
        }
        return postReadService.findPostByMemberIdPage(memberId,memberSessionId, pageable);

    }

    @Operation(summary = "사용자의 게시물 불러오기 , 무한 스크롤 // 로그인 했으면 차단된 게시물 제외")
    @GetMapping("/{member_id}/scroll")
    public PageScroll<PostPhotoDto> getPostScroll(@PathVariable Long member_id, ScrollRequest scrollRequest, HttpSession session){
        Long memberSessionId = sessionReadService.getUserDataNoThrow(session);

        if (memberSessionId == null){
            return postReadService.findPostByMemberIdCursor(member_id, scrollRequest);
        }

        PageScroll<PostPhotoDto> postByMemberId = postReadService.findPostByMemberIdCursor(member_id, memberSessionId, scrollRequest);
        return postByMemberId;
    }

    @Operation(summary = "타임라인 조회 // only RDBMS")
    @GetMapping("/timeLine")
    public PageScroll<PostPhotoDto> getTimeLine(ScrollRequest scrollRequest, HttpSession session){
        Member userData = sessionReadService.getUserData(session);
        PageScroll<PostPhotoDto> timeLine = postReadService.getTimeLine(userData.getId(), scrollRequest);
        return timeLine;
    }

    @Operation(summary = "좋아요, 취소")
    @PostMapping("postLike")
    public String postLike(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = postLikeWriteService.postLike(postLikeDto);
        return result;
    }

    @Operation(summary = "breakpoint, 좋아요 동시성 테스트")
    @PostMapping("postLike/example")
    public String postExample(@Valid @RequestBody PostLikeDto postLikeDto){
        String result = postLikeWriteService.postExample(postLikeDto);
        return result;
    }

}
